public class Vehicle 
{
	private String make;
	
	public String getMake()
	{
		return make;
	}
	
	public boolean setMake(String newMake)
	{
		if(newMake.length() > 0)
		{
			make = newMake;
			return true;
		}
		else
		{
			return false;
		}
	}
}
