package net.es.mp.measurement;

import net.es.mp.authn.AuthnSubject;
import net.es.mp.authz.AuthorizationException;
import net.es.mp.authz.AuthzAction;
import net.es.mp.measurement.types.Measurement;
import net.es.mp.measurement.types.validators.MeasurementValidator;
import net.es.mp.scheduler.NetLogger;
import net.es.mp.types.validators.InvalidMPTypeException;
import net.es.mp.util.IDUtil;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class MeasurementManager {
    Logger log = Logger.getLogger(MeasurementManager.class);
    Logger netLogger = Logger.getLogger("netLogger");
    
    final private String MEASUREMENT_COLLECTION = "measurements";
    
    final private String CREATE_EVENT = "mp.measurement.MeasurementManager.createMeasurement";
    final private String GET_EVENT = "mp.measurement.MeasurementManager.getMeasurement";
    
    public void createMeasurement(Measurement measurement, String uriPath) throws MPMeasurementException{
        NetLogger netLog = NetLogger.getTlogger();
        this.netLogger.debug(netLog.start(CREATE_EVENT));
        MPMeasurementService globals = MPMeasurementService.getInstance();
        //generate ID and uri
        String baseURI = globals.getContainer().getResourceURL();
        ObjectId id = new ObjectId();
        String uri = IDUtil.generateURI(baseURI, uriPath, id.toString());
        measurement.setID(id);
        measurement.setURI(uri);
        
        //validate 
        try {
            if(measurement.getType() != null &&
                    globals.getValidatorMap().containsKey(measurement.getType())){
                globals.getValidatorMap().get(measurement.getType()).validate(measurement);
            }else if(globals.getRequiresValidator()){
                throw new MPMeasurementException("Measurements of type " + measurement.getType()
                        + " are not supported by this server.");
            }else{
                (new MeasurementValidator()).validate(measurement);
            }
        } catch (InvalidMPTypeException e) {
            this.netLogger.debug(netLog.error(CREATE_EVENT, e.getMessage()));
            e.printStackTrace();
            throw new MPMeasurementException("Invalid measurement request: " + e.getMessage());
        }
        
        //store
        DB db = globals.getDatabase();
        DBCollection coll = db.getCollection(MEASUREMENT_COLLECTION);
        coll.insert(measurement.getDBObject());
        
        this.netLogger.debug(netLog.end(CREATE_EVENT));
    }
    
    public Measurement getMeasurement(String measurementId, AuthnSubject authnSubject) throws AuthorizationException {
        NetLogger netLog = NetLogger.getTlogger();
        this.netLogger.debug(netLog.start(GET_EVENT));
        
        //check if can query at all
        MPMeasurementService.getInstance().getAuthorizer().authorize(authnSubject, AuthzAction.QUERY, null);
        
        DB db = MPMeasurementService.getInstance().getDatabase();
        DBCollection coll = db.getCollection(MEASUREMENT_COLLECTION);
        Measurement measurement = null;
        try{
            //ObjectId constructor provides *some* validation
            DBObject dbObj = coll.findOne(new BasicDBObject("_id", new ObjectId(measurementId)));
            measurement = new Measurement(dbObj);
        }catch(Exception e){
            log.debug("ID not found: " + e.getMessage());
        }
        
        //check if can query at all
        MPMeasurementService.getInstance().getAuthorizer().authorize(authnSubject, AuthzAction.QUERY, measurement);
        
        this.netLogger.debug(netLog.end(GET_EVENT));
        return measurement;
    }
}
