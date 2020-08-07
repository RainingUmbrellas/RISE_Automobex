import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.collections4.*;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.lang3.exception.ExceptionUtils;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;



/**
 * Reads the CSV of various DataSets that make up the app's database
 * @author Alicia Key
 * @Date 8/7/2020
 *
 */
public class DatabaseReader {
	
	/**
	 * Will be empty unless the file being read is the Activity DataSet
	 */
	private ArrayList<Activity> _activityDataSet= new ArrayList<Activity>();
	private HashMap _dataSet = new HashMap();
	/**
	 * Used for Activity Completed dataSet to track the different activities each user completes
	 */
	private MultiValuedMap<String, String> _dataSet_MultiValued = new ArrayListValuedHashMap<>();
	/**
	 * Can be used for Activity, which has ActivityNo and name, which are similar keys
	 */
	private MultiKeyMap<String, Integer> _dataSet_MultiKey = new MultiKeyMap<String, Integer>();
	
	private FileReader _fileReader;
	private CSVReader _reader;
	public final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	
	public DatabaseReader(String fileName, boolean isActivityDataSet) {
		try {
			_fileReader = new FileReader(fileName);
			_reader = new CSVReaderBuilder(_fileReader).withSkipLines(2).build(); // skips 2 header lines 
			parse(isActivityDataSet);
		} catch (Exception e) {
			LOGGER.logp(Level.WARNING, "DatabaseReader","DatabaseReader","Exception in DatabaseReader constructor: " + ExceptionUtils.getStackTrace(e));
			
		} finally {
			close();
		}
	}
	
	public HashMap getDataSet() {
		return _dataSet;
	}
	
	/**
	 * Returns the ArrayList of Activity objects created from CSV file
	 * Will be empty unless the file being read is the Activity DataSet CSV file
	 */
	public ArrayList<Activity> getActivityDataSet(){
		return _activityDataSet;
	}
	
	public MultiValuedMap<String, String> getDataSet_MultiValued() {
		return _dataSet_MultiValued;
	}
	public  MultiKeyMap<String, Integer> getDataSet_MultiKey() {
		return _dataSet_MultiKey;
	}
	
	/**
	 * Parses CSV File to create 
	 */
	public void parse(boolean isActivityDataSet) {
		HashMap<String, String> dataset = new HashMap<String, String>();
		ArrayList<Activity> activityDataSet = new ArrayList<Activity>();
		MultiValuedMap<String, String> multiValued_Set = new ArrayListValuedHashMap<>();
		MultiKeyMap<String, Integer> multiKey_Set = new MultiKeyMap<String, Integer>();
		
		try {
			// loop through each line of the csv file:
			String[] nextline;
			while ((nextline = _reader.readNext()) != null) {
				if(isActivityDataSet) {
					Activity a = new Activity(nextline[0], nextline[1], Integer.parseInt(nextline[2]) );
					activityDataSet.add(a); 
					//multiKey_Set.put(nextline[0], nextline[1], Integer.parseInt(nextline[2]));
				}
				else {
					//dataset.put(nextline[0], nextline[1]);
					multiValued_Set.put(nextline[0], nextline[1]);
				}
				
			}
		} catch (Exception e) {
			LOGGER.logp(Level.WARNING, "DatabaseReader","parse","Exception in parse method: " + ExceptionUtils.getStackTrace(e));
						
		} finally {
			//close the file
			close();
		}
		
		_dataSet = dataset;
		_activityDataSet = activityDataSet;
		_dataSet_MultiValued = multiValued_Set;
		_dataSet_MultiKey = multiKey_Set;
	}
	
	
	
	public void close()  {
		try {
			_fileReader.close();
		} catch (IOException e) {
			LOGGER.logp(Level.WARNING, "readers.Reader", "close", "IOException in Reader. " + ExceptionUtils.getStackTrace(e));
		}
	}
	
	
}
