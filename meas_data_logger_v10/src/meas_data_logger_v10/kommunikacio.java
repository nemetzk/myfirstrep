package meas_data_logger_v10;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import java.io.InputStream;
import java.io.IOException;

public class kommunikacio implements kommunikacio_interface {
	public static  final int MAXIMUM_DATA_IDX = 254;
	public static  int curr_data_max_idx = 0;
	public static  int[] kiolvasott_adatok;
	public static  int[] kiolvasott_feszultseg;
	public static  int[] kiolvasott_feszultseg_min;
	public static  int[] kiolvasott_feszultseg_max;
	public static  int[] kiolvasott_aram;

	public static  int komm_receive_automata_state = 0;
	public static  char komm_input_data = 0;
	int komm_input_data_len = 0;
	public static  int komm_station_number = 0;
	public static  int komm_address = 0;
	public static  int komm_data_code = 0;
	 static int digit_mid_value = 0;
	 static int digit_interpreter_state = 0;
	public static  final int DIGIT_INTERPRETER_INIT_STATE = 0;
	public static  final int DIGIT_INTERPRETER_NEXT_DIGIT_STATE = 1;
	 static int digit_current_character_count = 0;
	 static int digit_interpreter_result = 0;
	 static boolean digit_interpreter_error_flg = false;

	 static int read_data_idx = 0;
	 int read_data_idx_max = 0;
	 static boolean datastream_completed_flg = false;
	 static timer COMMUNICATION_TIMEOUT = new timer();
	 final static int COMMUNICATION_TIMEOUT_TIME = 100;
	 int komm_receive_automata_state_prev = 0;
	 static SerialPort chosenPort;
	 int x_index = 0;
	 static int kommunikacios_csatorna = 2;
	 private static int measureIndex = 0;
	 public static boolean drawOnlineGraph = true;
	public static void UDP_init() {
		int PACKETSIZE = 4000;

		Thread UDP_thread = new Thread() {
			@Override
			public  void run() {
				try {
					// Convert the argument to ensure that is it valid
					int port = 4000;
					int _byte = 0;
					// Construct the socket
					DatagramSocket socket = new DatagramSocket(port);
					System.out.println("The server is ready...");
					for (;;) {
						resetMeasureIndex();
						DatagramPacket packet = new DatagramPacket(new byte[PACKETSIZE], PACKETSIZE);
						socket.receive(packet);
						String inputData;
						inputData = new String(packet.getData());
						InputStream is = new ByteArrayInputStream(inputData.getBytes());
						System.out.println(
								packet.getAddress() + " " + packet.getPort() + ": " + new String(packet.getData()));
						try {
							while ((_byte = is.read()) != -1) {
								// System.out.print((char) _byte);
								kommunikacio.komm_input_data = (char) _byte;
								kommunikacio.komm_adatok_erkeztek_automata();
							}
						} catch (IOException e) {
							// System.out.print((char) _byte);
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					} // for
				} // try
				catch (Exception e) {
					System.out.println(e);
				} // catch
			} // void
		}; // thread
		UDP_thread.start();
	}// void

	public static void serial_port_init() {
		chosenPort.setComPortParameters(57600, 8, 1, 0);
		if (chosenPort.openPort()) {
			// connectButton.setText("Disconnect");
			// portList.setEnabled(false);
		}

		Thread infinite_thread = new Thread() {
			@Override
			public  void run() {
				int _byte = 0;
				InputStream is = kommunikacio.chosenPort.getInputStream();
				// while (true){

				try {
					while ((_byte = is.read()) != -1) {
						// System.out.print((char) _byte);
						kommunikacio.komm_input_data = (char) _byte;
						kommunikacio.komm_adatok_erkeztek_automata();
					}
				} catch (IOException e) {
					// System.out.print((char) _byte);
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		infinite_thread.setDaemon(true);
		infinite_thread.start();

	}

	public static void kommunikacio_init() {
		kiolvasott_adatok = new int[MAXIMUM_DATA_IDX + 1];
		kiolvasott_feszultseg = new int[MAXIMUM_DATA_IDX + 1];
		kiolvasott_feszultseg_min = new int[MAXIMUM_DATA_IDX + 1];
		kiolvasott_feszultseg_max = new int[MAXIMUM_DATA_IDX + 1];
		kiolvasott_aram = new int[MAXIMUM_DATA_IDX + 1];
		timer COMMUNICATION_TIMEOUT = new timer();
		drawOnlineGraph = true;
		if (kommunikacios_csatorna == SOROS_PORT) {
			serial_port_init();
		} else if (kommunikacios_csatorna == UDP) {
			UDP_init();
		}

	}

	public static boolean check_comm_timeouts() { // ki kell dolgozni!
		boolean completed_flg = false;
		return completed_flg;
	}

	public static void komm_adatok_erkeztek(String my_input_data) {
		int input_chr_idx = 0;
		komm_receive_automata_state = KRA_INIT;
		for (input_chr_idx = 0; (input_chr_idx <= my_input_data.length()); input_chr_idx++) {
			komm_input_data = my_input_data.charAt(input_chr_idx + 1);
			komm_adatok_erkeztek_automata();
		}
	}

	public static  void komm_adatok_erkeztek_automata() {
		/*
		 * if (COMMUNICATION_TIMEOUT.timerElapsed()){
		 * komm_receive_automata_state=KRA_INIT; }
		 */
		// System.out.print((char) komm_input_data);
		switch (komm_receive_automata_state) {
		case KRA_INIT:// 0
			if (komm_input_data == OPEN_CHAR) {
				if (datastream_completed_flg) {
					datastream_completed_flg = false;
				}
				COMMUNICATION_TIMEOUT.setTimer(COMMUNICATION_TIMEOUT_TIME);
				komm_receive_automata_state = KRA_WHOAMI;
			}
			break;

		case KRA_WHOAMI: // 1 ? master vagyis kerdezo vagy ! slave vagyis valaszolo
			if (komm_input_data == CHAR_SLAVE) {
				komm_receive_automata_state = KRA_SEMI2;
			} else {
				COMMUNICATION_TIMEOUT.setTimer(COMMUNICATION_TIMEOUT_TIME);
				komm_receive_automata_state = KRA_INIT;
			}
			digit_interpreter_init();
			break;

		case KRA_SEMI2:// 2
			if (komm_input_data == CHAR_SEPARATOR) {
				COMMUNICATION_TIMEOUT.setTimer(COMMUNICATION_TIMEOUT_TIME);
				komm_receive_automata_state = KRA_READ_STATION;
			} else {
				komm_receive_automata_state = KRA_INIT;
			}
			break;

		case KRA_READ_STATION:// 3
			if (digit_interpreter(komm_input_data)) {
				komm_station_number = digit_interpreter_result;
				digit_interpreter_init();
				COMMUNICATION_TIMEOUT.setTimer(COMMUNICATION_TIMEOUT_TIME);
				komm_receive_automata_state = KRA_READ_ADDRESS;
			}
			break;

		case KRA_READ_ADDRESS:// 4
			if (digit_interpreter(komm_input_data)) {
				komm_address = digit_interpreter_result;
				digit_interpreter_init();
				COMMUNICATION_TIMEOUT.setTimer(COMMUNICATION_TIMEOUT_TIME);
				komm_receive_automata_state = KRA_READ_DATA_CODE;
			}
			break;

		case KRA_READ_DATA_CODE:// 5
			if (digit_interpreter(komm_input_data)) {
				komm_data_code = digit_interpreter_result;
				digit_interpreter_init();
				read_data_idx = 0;
				COMMUNICATION_TIMEOUT.setTimer(COMMUNICATION_TIMEOUT_TIME);
				komm_receive_automata_state = KRA_READ_DATA_ARRAY;
			}
			break;

		case KRA_READ_DATA_ARRAY:// 6
			if (digit_interpreter(komm_input_data)) {
				digit_interpreter_init();
				if ((komm_input_data != '>') && (read_data_idx < MAXIMUM_DATA_IDX)) {
					kiolvasott_adatok[read_data_idx] = digit_interpreter_result;
					// listaba_toltes (read_data_idx, digit_interpreter_result);
					read_data_idx = read_data_idx + 1;
					curr_data_max_idx = read_data_idx;
				} else {
					komm_receive_automata_state = KRA_END;
				}
			}
			break;

		case KRA_END:// 7
			start_data_separator(komm_data_code);
			
			//file_saver.input_array = kiolvasott_adatok;
			//file_saver.save_file();
			newMeasureIndex();
			meas_data_logger_main_frame.myDatabaseObject.insertRow(measureIndex,meas_data_logger_main_frame.generateDataFieldNames(),kiolvasott_adatok);
			
			//if (i!=0) valueString+=",";if (i!=0) valueString+=",";if (i!=0) valueString+=",";if (i!=0) valueString+=",";if (i!=0) valueString+=",";if (i!=0) valueString+=",";
			// diagram_init;
			// draw_diagram (komm_data_code);
			// save_to_file (prepare_data_to_save);
			komm_receive_automata_state = KRA_INIT;
			break;
		} // switch
		/*
		 * if (komm_receive_automata_state_prev!=komm_receive_automata_state){
		 * System.out.print("RA("+komm_input_data+"): "+(int)
		 * komm_receive_automata_state+"; ");
		 * komm_receive_automata_state_prev=komm_receive_automata_state; }
		 */

	}

	private static void digit_interpreter_init() {
		digit_mid_value = 0;
		digit_interpreter_error_flg = false;
		digit_current_character_count = 0;
		digit_interpreter_state = DIGIT_INTERPRETER_INIT_STATE;
	}

	private static boolean digit_interpreter(char current_character) {
		boolean completed_flg = false;
		if (((current_character >= 48) && (current_character <= 57)) || (current_character == CHAR_SEPARATOR)) {
			switch (digit_interpreter_state) {
			case DIGIT_INTERPRETER_INIT_STATE:
				if (current_character != ';') {
					digit_mid_value = Character.getNumericValue(current_character);
				}
				digit_current_character_count = 1;
				digit_interpreter_result = 0;
				digit_interpreter_state = DIGIT_INTERPRETER_NEXT_DIGIT_STATE;
				break;
			case DIGIT_INTERPRETER_NEXT_DIGIT_STATE:
				if (current_character != CHAR_SEPARATOR) {
					digit_mid_value = (10 * digit_mid_value) + Character.getNumericValue(current_character);
					digit_current_character_count = digit_current_character_count + 1;
				} else {
					completed_flg = true;
					digit_interpreter_state = DIGIT_INTERPRETER_INIT_STATE;
					digit_interpreter_result = digit_mid_value;
				}
				break;
			}
		} else {
			digit_interpreter_error_flg = true;
			completed_flg = true;
		}
		return completed_flg;
	}

	public static void start_data_separator(int curr_data_code) {
		int sep_idx = 0;

		switch (curr_data_code) {
		case PCMSG_TYPE_CONTINUOUS_DATA:
			meas_data_logger_main_frame.mcf.getXYSeries(0).getSeries(0).clear();
			for (sep_idx = 0; (sep_idx <= MAXIMUM_DATA_IDX); sep_idx++) {
				kiolvasott_feszultseg[sep_idx] = kiolvasott_adatok[sep_idx];
				System.out.print("fesz(" + sep_idx + ")= " + kiolvasott_feszultseg[sep_idx] + "; ");
				if (drawOnlineGraph) meas_data_logger_main_frame.draw_new_feszultesg(sep_idx, kiolvasott_feszultseg[sep_idx]);
			}

			break;

		case PCMSG_TYPE_CONTINUOUS_MIN:
			for (sep_idx = 0; sep_idx <= MAXIMUM_DATA_IDX; sep_idx++) {
				kiolvasott_feszultseg_min[sep_idx] = kiolvasott_adatok[sep_idx];
			}
			break;

		case PCMSG_TYPE_CONTINUOUS_MAX:
			for (sep_idx = 0; sep_idx <= MAXIMUM_DATA_IDX; sep_idx++) {
				kiolvasott_feszultseg_max[sep_idx] = kiolvasott_adatok[sep_idx];
			}
			break;

		case PCMSG_TYPE_CURRENT_DATA:
			meas_data_logger_main_frame.mcf.getXYSeries(1).getSeries(0).clear();
			float imp = 0;
			for (sep_idx = 0; sep_idx <= MAXIMUM_DATA_IDX; sep_idx++) {
				kiolvasott_aram[sep_idx] = kiolvasott_adatok[sep_idx];
				System.out.print("aram(" + sep_idx + ")= " + kiolvasott_aram[sep_idx] + "; ");
				if (drawOnlineGraph) meas_data_logger_main_frame.draw_new_aram(sep_idx, kiolvasott_aram[sep_idx]);
				if (kiolvasott_aram[sep_idx] > 0)
					imp = kiolvasott_feszultseg[sep_idx] / kiolvasott_aram[sep_idx];
				else
					imp = 0;
				if (drawOnlineGraph) meas_data_logger_main_frame.draw_new_imp(sep_idx, (int) (100 * imp));
			}
			break;

		case PCMSG_TYPE_CURRENT_MIN:
			break;

		case PCMSG_TYPE_CURRENT_MAX:
			break;
		}
	}

	public static void resetMeasureIndex() {
		measureIndex=0;
	}
	
	public static int seeMeasureIndex() {
		return measureIndex;
	}
	
	public static void newMeasureIndex() {
		measureIndex++;
	}
} // class
