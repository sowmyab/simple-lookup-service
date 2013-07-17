package net.es.lookup.common;


public class ReservedKeys {

    //record-level keys
    public static final String RECORD_TYPE = "type";
    public static final String RECORD_TTL = "ttl";
    public static final String RECORD_URI = "uri";
    public static final String RECORD_OPERATOR = "operator";
    public static final String RECORD_EXPIRES = "expires";
    public static final String RECORD_SKIP = "skip";
    public static final String RECORD_MAXRESULTS = "maxresults";
    public static final String RECORD_PRIVATEKEY = "privatekey";
    public static final String RECORD_STATE = "state";

    //error keys
    public static final String ERROR_MESSAGE = "error-message";
    public static final String ERROR_CODE = "error-code";

    //subscribe keys
    public static final String RECORD_SUBSCRIBE_QUEUE = "subscribe-queue";
    public static final String RECORD_SUBSCRIBE_LOCATOR = "subscribe-locator";

    //server keys
    public static final String SERVER_STATUS_UNKNOWN = "unknown";
    public static final String SERVER_STATUS_ALIVE = "alive";
    public static final String SERVER_STATUS_UNREACHABLE = "unreachable";

    //operator keys
    public static final String RECORD_OPERATOR_SUFFIX = "operator";

    //group keys
    public static final String RECORD_GROUP_DOMAINS = "group-domains" ;

    //location keys
    public static final String RECORD_LOCATION_SITENAME = "location-sitename";
    public static final String RECORD_LOCATION_CITY = "location-city";
    public static final String RECORD_LOCATION_REGION = "location-region";
    public static final String RECORD_LOCATION_COUNTRY = "location-country";
    public static final String RECORD_LOCATION_ZIPCODE = "location-zipcode";
    public static final String RECORD_LOCATION_LATITUDE = "location-latitude";
    public static final String RECORD_LOCATION_LONGITUDE = "location-longitude";

    //service keys
    public static final String RECORD_SERVICE_NAME = "service-name";
    public static final String RECORD_SERVICE_VERSION = "service-version";
    public static final String RECORD_SERVICE_TYPE = "service-type";
    public static final String RECORD_SERVICE_LOCATOR = "service-locator";
    public static final String RECORD_SERVICE_ADMINISTRATORS = "service-administrators" ;
    public static final String RECORD_SERVICE_HOST = "service-host";

    //interface keys
    public static final String RECORD_INTERFACE_ADDRESSES = "interface-addresses";
    public static final String RECORD_INTERFACE_SUBNET = "interface-subnet" ;
    public static final String RECORD_INTERFACE_CAPACITY = "interface-capacity" ;
    public static final String RECORD_INTERFACE_MACADDRESS = "interface-mac";

    //host keys
    public static final String RECORD_HOST_NET_INTERFACES = "host-net-interfaces";
    public static final String RECORD_HOST_NET_TCP_MAXBACKLOG = "host-net-tcp-maxbacklog";
    public static final String RECORD_HOST_NET_TCP_AUTOTUNEMAXBUFFER_SEND = "host-net-tcp-autotunemaxbuffer-send";
    public static final String RECORD_HOST_NET_TCP_AUTOTUNEMAXBUFFER_RECV = "host-net-tcp-autotunemaxbuffer-recv";
    public static final String RECORD_HOST_NET_TCP_MAXBUFFER_SEND = "host-net-tcp-maxbuffer-send";
    public static final String RECORD_HOST_NET_TCP_MAXBUFFER_RECV = "host-net-tcp-maxbuffer-recv";
    public static final String RECORD_HOST_NET_TCP_CONGESTIONALGORITHM = "host-net-tcp-congestionalgorithm";
    public static final String RECORD_HOST_OS_NAME = "host-os-name" ;
    public static final String RECORD_HOST_OS_VERSION = "host-os-version";
    public static final String RECORD_HOST_OS_KERNEL = "host-os-kernel";
    public static final String RECORD_HOST_NAME = "host-name";
    public static final String RECORD_HOST_HARDWARE_MEMORY = "host-hardware-memory";
    public static final String RECORD_HOST_HARDWARE_PROCESSORSPEED = "host-hardware-processorspeed";
    public static final String RECORD_HOST_HARDWARE_PROCESSORCOUNT = "host-hardware-processorcount";
    public static final String RECORD_HOST_HARDWARE_PROCESSORCORE = "host-hardware-processorcore";

    //person keys
    public static final String RECORD_PERSON_EMAILIDS = "person-emails";
    public static final String RECORD_PERSON_PHONENUMBERS = "person-phonenumbers";
    public static final String RECORD_PERSON_ORGANIZATION = "person-organization";
    public static final String RECORD_PERSON_NAME = "person-name";
}