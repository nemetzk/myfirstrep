/**
 * 
 */
package datum_gyakorlo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author kn
 *
 */
public class dgy_main {

	/**
	 * 
	 */
	public dgy_main() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String myDate = dateFormat.format(date);
		System.out.println(myDate);

	}

}
