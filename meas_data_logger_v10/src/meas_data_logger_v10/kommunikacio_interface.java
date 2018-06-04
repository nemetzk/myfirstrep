package meas_data_logger_v10;

public interface kommunikacio_interface {

	public static final char OPEN_CHAR = '<';
	public static final char CLOSE_CHAR = '>';
	public final static char CHAR_MASTER = '?';
	public final static char CHAR_SLAVE = '!' ;
	public final static char CHAR_SEPARATOR = ';';
	public static final int KRA_INIT = 0;
	public static final int KRA_WHOAMI = 1;
	public static final int KRA_SEMI2 = 2;
	public static final int KRA_READ_STATION = 3;
	public static final int KRA_READ_ADDRESS = 4;
	public static final int KRA_READ_DATA_CODE = 5;
	public static final int KRA_READ_DATA_ARRAY = 6;
	public static final int KRA_END = 7;

	
	public static final int PCMSG_TYPE_PHMONITOR_DATA   =   1;
	public static final int PCMSG_TYPE_PHSETTABLE_DATA  =   2;
	public static final int PCMSG_TYPE_CONTINUOUS_DATA  =   3;
	public static final int PCMSG_TYPE_CONTINUOUS_MIN   =   4;
	public static final int PCMSG_TYPE_CONTINUOUS_MAX   =   5;
	public static final int PCMSG_TYPE_PUPMAX_DATA      =   6;
	public static final int PCMSG_TYPE_PUPMAX_MIN       =   7;
	public static final int PCMSG_TYPE_PUPMAX_MAX       =   8;
	public static final int PCMSG_TYPE_CURRENT_DATA     =   9;
	public static final int PCMSG_TYPE_CURRENT_MIN      =   10;
	public static final int PCMSG_TYPE_CURRENT_MAX      =   11;
	public static final int PCMSG_TYPE_IMPEDANCE_DATA   =   12;
	public static final int PCMSG_TYPE_IMPEDANCE_MIN    =   13;
	public static final int PCMSG_TYPE_IMPEDANCE_MAX    =   14;
	public static final int PCMSG_TYPE_POWER_DATA       =   15;
	public static final int PCMSG_TYPE_POWER_MIN        =   16;
	public static final int PCMSG_TYPE_POWER_MAX        =   17;

	public static final int SOROS_PORT = 1;
	public static final int UDP = 2;

}

