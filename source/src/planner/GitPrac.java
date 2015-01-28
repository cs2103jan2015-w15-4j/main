package planner;

/**
 * 
 * @author kohwaikit
 * This class is used for basic git practice. Remove when all is done.
 * 
 * Instructions: remove comment on your own name and add a println there.
 */
public class GitPrac {

	public static void pauseForASec(){
		
		long start = System.currentTimeMillis();
		while( System.currentTimeMillis() != start + 1000);
	}
	
	public static void countDown(){
		
		for( long i = 5; i >= 1; --i ){
			
			pauseForASec();
			System.out.println(i);
		}
		
		System.out.println();
		pauseForASec();
	}
	
	public static void main(String[] args) {
		
		countDown();
		
		System.out.println("Wai Kit was here.");
		System.out.println("Zheng Yi was here too.");
		System.out.println("Ke Jing was here three");
		System.out.println("Jia Jun was here :)");
		System.out.println("Hello");

	}

}
