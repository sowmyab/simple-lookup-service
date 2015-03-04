package net.es.lookup.utils.config.reader;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: sowmya
 * Date: 3/26/13
 * Time: 11:14 AM
 */
public class QueueServiceConfigReader {

    private static QueueServiceConfigReader instance;
    private static final String DEFAULT_FILE = "queueservice.yaml";
    private static final String DEFAULT_PATH = "etc";
    private static String configFile = DEFAULT_PATH + "/" + DEFAULT_FILE;

    private int port = 61617;
    private List<String> host;
    private String protocol = "tcp";

    private boolean serviceOn = false;

    private boolean persistent = false;

    private long ttl = 120000;

    private int batchSize=10;
    private int pushInterval = 120;

    private static Logger LOG = Logger.getLogger(BaseConfigReader.class);
    

    /**
     * Constructor - private because this is a Singleton
     */
    private QueueServiceConfigReader() {

        host = new ArrayList<String>();

    }

    /*
   * set the config file
   * */

    public static void init(String cFile) {

        configFile = cFile;
    }

    /**
     * @return the initialized QueueServiceConfigReader singleton instance
     */
    public static QueueServiceConfigReader getInstance() {

        if (QueueServiceConfigReader.instance == null) {
            QueueServiceConfigReader.instance = new QueueServiceConfigReader();
            QueueServiceConfigReader.instance.setInfo(configFile);
        }
        return QueueServiceConfigReader.instance;
    }

    public List<String> getHost() {

        return this.host;
    }

    public int getPort() {

        return this.port;
    }

    public String getProtocol() {

        return protocol;
    }

    public long getTtl() {

        return ttl;
    }


    public boolean isQueuePersistent() {

        return persistent;
    }

    public String getUrl(){

        String url=""; //= protocol + "://" + host + ":" + port+"?wireFormat.maxInactivityDuration=600000";
        url = "failover:(";
        for(String h:host){
            url += protocol + "://"+ h + ":" + port+"?wireFormat.maxInactivityDuration=600000,";

        }

        url += "?randomize=false";
        System.out.println(url);
        return url;
    }

    public int getBatchSize() {

        return batchSize;
    }

    public int getPushInterval() {

        return pushInterval;
    }

    private void setInfo(String configFile) {

        BaseConfigReader cfg = BaseConfigReader.getInstance();
        Map yamlMap = cfg.getConfiguration(configFile);
        assert yamlMap != null : "Could not load configuration file from " +
                "file: ${basedir}/" + configFile;


        try {

            HashMap<String, Object> queueServiceMap = (HashMap) yamlMap.get("queue");
            host = (List<String>) queueServiceMap.get("host");
            port = (Integer) queueServiceMap.get("port");
            String service = (String) queueServiceMap.get("queueservice");
            if(service.equals("on")){
                serviceOn = true;
            }else{
                serviceOn=false;
            }
            batchSize = (Integer) queueServiceMap.get("batch_size");
            pushInterval = (Integer) queueServiceMap.get("push_interval");

            HashMap<String, Object> messageMap = (HashMap) yamlMap.get("message");
            persistent = (Boolean) messageMap.get("persistent");
            ttl = ((Integer) messageMap.get("ttl")) * 1000;



        } catch (Exception e) {
            LOG.error("Error parsing config file; Please check config parameters" + e.toString());
            System.exit(1);
        }


    }

    public boolean isServiceOn() {

        return serviceOn;
    }
}
