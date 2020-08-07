import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.MultiKeyMap;

/**
 * 
 * @author Alicia Key
 * @Date 8/7/2020
 */
public class Main {
	/**
	 * Holds UserID and Username (and any other information needed)
	 * MultiValuedMap is used because the userId key can be associated with more than 1 value
	 */
	public static MultiValuedMap<String, String> _users;  //public static HashMap<String, String> _users;
	
	/**
	 * List of Activity objects that each hold ActivityNo., name, and PointWorth
	 */
	public static ArrayList<Activity> _activity;
	
	/**
	 * Holds UserId and Activity No.
	 */
	public static MultiValuedMap<String, String> _activityCompleted; //public static HashMap<String, String> _activityCompleted;
	
	/**
	 * Holds UserId and Total Points
	 */
	public static HashMap<String, Integer> _usersPointTotal;
	
	/**
	 * Holds Activity No. and Percentage of Users who completed it
	 */
	public static HashMap<String, Double> _activityBreakdown;
	
	
	public static void main(String[] args) {
		String activityCSV = "Useful Docs/Activities Table.csv";
		String activityCompletedCSV = "Useful Docs/Activities Completed Table.csv";
		String usersCSV = "Useful Docs/Users Table.csv";
		
		DatabaseReader dbrUsers = new DatabaseReader(usersCSV, false);
		DatabaseReader dbrActivity = new DatabaseReader(activityCSV, true);
		DatabaseReader dbrActivityCompleted = new DatabaseReader(activityCompletedCSV, false);

		_users = dbrUsers.getDataSet_MultiValued(); 
		_activity = dbrActivity.getActivityDataSet();
		_activityCompleted = dbrActivityCompleted.getDataSet_MultiValued();
		
		System.out.println("+++++++++ USER'S TOTAL POINTS +++++++++");
		_usersPointTotal = pointsCalculation();
		System.out.println(usersPointTotal_ToString());
		
		System.out.println("+++++++++ ACTIVITY BREAKDOWN +++++++++");
		_activityBreakdown = percentageOfActivityCompleted();
		System.out.println(percentageCompleted_ToString());
		
	}
	
	
	/**
	 * Calculates an individual user's point
	 * @return user's total points 
	 */
	private static int userPointCalculation(String userId, Collection<String> activitiesCompleted) {
		int totalPoints = 0;
		
		// loop through activitiesCompleted
		for (String s : activitiesCompleted) {
			for (Activity a : _activity) {
				if (a.getActivityNo().equals(s)) {
					totalPoints += a.getPointWorth();
					// Add to _numUsersCompleted
					 a.addToNumUsersCompleted();
				}
			}
		}
		return totalPoints;
	}
	
	
	/**
	 * Calculates every user's points
	 * @return HashMap of userIds and corresponding points
	 */
	private static HashMap<String, Integer> pointsCalculation(){
		HashMap<String, Integer> usersPointTotal = new HashMap<String, Integer>();
		ArrayList<String> usersIds = new ArrayList<>(_users.keySet());
		
		
		// loop over userIds 
		for(int index = 0; index < usersIds.size(); index++) {
			Collection<String> completedActivity = _activityCompleted.get(usersIds.get(index));
			int currentUserTotal = userPointCalculation(usersIds.get(index), completedActivity);
			usersPointTotal.put(usersIds.get(index), currentUserTotal);
		}
		_usersPointTotal = usersPointTotal;
		return usersPointTotal; 
	}
	
	
	/**
	 * Returns string version of the hashmap of user's total points [UsersIds TotalPoints]
	 * @return string version of the hashmap of user's total points
	 */
	public static String usersPointTotal_ToString() {
		StringBuffer s = new StringBuffer();
		for (Entry<String, Integer> e : _usersPointTotal.entrySet()) 
            s.append(e.getKey() + " " + e.getValue() + "\n"); 
		return s.toString();
	}
	
	
	
	/**
	 * Calculates the percentage of users completed each activity
	 * @return HashMap of the ActivityNo and corresponding percentages
	 */
	private static HashMap<String, Double> percentageOfActivityCompleted() {
		HashMap<String, Double> percentageActivityCompleted = new HashMap<String, Double> ();
		double totalIndividualPercentage = 0.0;
		
		// individual percentage: numCompleted/total num users
		for(Activity a: _activity) {
			a.setPercentage( (double)a.getNumUsersCompleted() /(double)_users.keys().size() );
			totalIndividualPercentage += a.getPercentage();
		}
		
		// loop list of Activities
		for(Activity a: _activity) { 
			percentageActivityCompleted.put(a.getActivityNo(), ( (a.getPercentage()/totalIndividualPercentage))* 100.0 );
		}
		return percentageActivityCompleted;
	}
	
	/**
	 * Returns string version of the hashmap of activity breakdown
	 * @return string version of the hashmap of activity breakdown
	 */
	public static String percentageCompleted_ToString() {
		StringBuffer s = new StringBuffer();
		for (Entry<String, Double> e : _activityBreakdown.entrySet()) 
            s.append(e.getKey() + " " + e.getValue() + "\n"); 
		return s.toString();
	}
	
}
