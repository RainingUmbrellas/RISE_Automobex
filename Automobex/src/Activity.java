/**
 * Representation of a Sustainable Activity
 * @author Alicia Key
 * @Date 8/7/2020
 */
public class Activity {

	private String _activityNo;
	private String _name;
	private int _pointWorth;
	/**
	 * Number of users who completed this activity
	 */
	private int _numUsersCompleted = 0;
	/**
	 * Percentage of users who completed this activity
	 */
	private double _IndividualPercentage;
	
	public Activity(String num, String actName, int pointValue) {
		_activityNo = num;
		_name = actName;
		_pointWorth = pointValue;
	}
	
	public String getActivityNo() {
		return _activityNo;
	}
	public String getActivityName() {
		return _name;
	}
	public int getPointWorth() {
		return _pointWorth;
	}
	public int getNumUsersCompleted() {
		return _numUsersCompleted;
	}
	
	public void addToNumUsersCompleted() {
		_numUsersCompleted++;
	}
	
	public double getPercentage() {
		return _IndividualPercentage;
	}
	public void setPercentage(double percentage) {
		_IndividualPercentage = percentage;
	}
	
	@Override
	public String toString() {
		StringBuffer s = new StringBuffer(_name + ": \t");
		s.append(_activityNo + "\t");
		s.append(_pointWorth);
		return s.toString();
	}
}
