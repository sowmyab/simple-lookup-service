package net.es.lookup.publish;

import net.es.lookup.common.Message;
import net.es.lookup.common.exception.internal.DataFormatException;
import net.es.lookup.common.exception.internal.DatabaseException;
import net.es.lookup.database.DBPool;
import net.es.lookup.database.ServiceDAOMongoDb;
import net.es.lookup.protocol.json.JSONMessage;
import org.apache.log4j.Logger;
import org.quartz.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Author: sowmya
 * Date: 3/1/16
 * Time: 2:52 PM
 */
public class PublisherScheduler implements Job {
    private static Logger LOG = Logger.getLogger(PublisherScheduler.class);

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Date now = new Date();

        long start = now.getTime();

        LOG.info("Executing Publisher Scheduler");
        LOG.debug("Start time: "+start);

        Publisher publisher = Publisher.getInstance();
        Collection<Queue> queues = publisher.getAllQueues();

        ServiceDAOMongoDb db = DBPool.getDb("lookup");

        for (Queue queue : queues) {

            long interval = queue.getTimeInterval();

            Date nextPushTime = new Date(queue.getLastPushed().getTime() + interval);

            //Push to queue if "x" minutes have passed since last push or if the max Events threshold has reached
            if (nextPushTime.before(now) || (queue.getCurrentPushEvents() >= queue.getMaxPushEvents())) {

                LOG.debug("Time to send messages to Queue: "+nextPushTime.before(now));
                LOG.debug("Messages within max events? "+(queue.getCurrentPushEvents() >= queue.getMaxPushEvents()));

                List<Message> messages = null;
                try {
                    //DB Optimization - Query only if there are any events
                    if(queue.getCurrentPushEvents()>0) {
                        LOG.debug("Querying time range:" + queue.getLastPushed().toString() + "---" + now.toString());
                        messages = db.findRecordsInTimeRange(queue.getLastPushed(), now);

                    }

                } catch (DatabaseException e) {
                    e.printStackTrace();
                }

                //Push to queue only if there are messages to send

                if (messages != null && !messages.isEmpty()) {
                    Date overhead = new Date();
                    long overheadTime = overhead.getTime();
                    long overheadProcessingTime = overheadTime-start;
                    LOG.debug("Overhead processing in Publisher scheduler"+overheadProcessingTime);

                    try {
                        for(Message message: messages){
                            String jsonMessage = JSONMessage.toString(message);
                            LOG.debug("net.es.lookup.publish.PublisherScheduler:"+ jsonMessage);


                            JobDetail publishInvoker = newJob(PublishJob.class)
                                    .withIdentity("publish"+message.getURI(), "pubsub")
                                    .build();

                            SimpleTrigger publishTrigger = (SimpleTrigger) newTrigger().withIdentity("publish trigger"+message.getURI(), "pubsub")
                                    .startNow()
                                    .build();



                            publishInvoker.getJobDataMap().put(PublishJob.MESSAGE, jsonMessage);
                            publishInvoker.getJobDataMap().put(PublishJob.QUEUE, queue);

                            net.es.lookup.timer.Scheduler.getInstance().schedule(publishInvoker, publishTrigger);

                        }


                    } catch (DataFormatException e) {
                        LOG.error("net.es.lookup.publish.PublisherScheduler: "+e.getMessage());
                    }

                    queue.setLastPushed(now);
                    queue.setCurrentPushEvents(0);


                }else{
                    LOG.info("net.es.lookup.publish.PublisherScheduler:No messages to send");
                }

            }

        }
        Date end = new Date();
        long endTime = end.getTime();
        long totalProcessingTime = endTime-start;
        LOG.debug("net.es.lookup.publish.PublisherScheduler: Total time to Execute Publisher Scheduler "+totalProcessingTime);
    }

}