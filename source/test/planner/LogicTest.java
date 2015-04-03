package planner;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class LogicTest {
    private Logic targetClass;
    
    public void initialize(){
        targetClass = new Logic();
    }
    
    public TaskList initializeList() throws Exception {
        Calendar calFirst = Calendar.getInstance();
        calFirst.set(2015, 5, 3, 0, 0);
        
        Calendar calSecond = Calendar.getInstance();
        calSecond.set(2015, 5, 8, 23, 59);
        
        Calendar calThird = Calendar.getInstance();
        calThird.set(2015, 5, 3, 23, 59);
        
        Calendar calFourth = Calendar.getInstance();
        calFourth.set(2015, 5, 10, 23, 59);

        Calendar calOverDue = Calendar.getInstance();
        calOverDue.set(2015, 2, 20, 23, 59);
        
        Calendar calOverDue1 = Calendar.getInstance();
        calOverDue1.set(2015, 3, 3, 0, 0);
        
        Calendar calOverDue2 = Calendar.getInstance();
        calOverDue2.set(2015, 2, 23, 23, 59);
        
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/09/2015"); 
        
        Task task1 = new Task("THIS IS A DUEDATE TASK", "NOTHING", date, 1, "work", 1);
        Task task2 = new Task("THIS IS A TIMEDTASK", "SPREAD THROUGHOUT SEVERAL DAYS", calFirst.getTime(), 2, "workload", 2);
        Task task3 = new Task("THIS IS ANOTHER TIMEDTASK", "WITHIN ONE DAY", calFirst.getTime(), 2, "workload", 3);
        Task task4 = new Task("THIS IS AN OVERDUED TASK", "DUEDATE TASK", calOverDue.getTime(), 4, "nothing", 4);
        Task task5 = new Task("THIS IS AN OVERDUED TIMED TASK", "STILL ONGOING", calOverDue.getTime(), 3, "this is a tag", 5);
        Task task6 = new Task("THIS IS AN OVERDUED TIMED TASK", "LONG PAST", calOverDue.getTime(), 5, "tags cannot be searched can it", 6);
        Task task7 = new Task("THIS IS A FLOATING TASK", "NULL DATE", null, 2, "can you find this", 7);
        Task task8 = new Task("THIS IS A FLOATING TASK", "NULL DATE", null, 2, "these are tags", 8);
        Task task9 = new Task("THIS IS A FLOATING TASK", "HIGHEST PRIORITY", null, 5, "can you dig it", 9);
        Task task10 = new Task("THIS IS A RANDOM DUEDATE TASK", "", calFourth.getTime(), 4, "", 10);
        
        task2.setEndDate(calSecond.getTime());
        task3.setEndDate(calThird.getTime());
        task5.setEndDate(calOverDue1.getTime());
        task6.setEndDate(calOverDue2.getTime());
        
        TaskList TL1 = new TaskList();
        TL1.add(task1);
        TL1.add(task2);
        TL1.add(task3);
        TL1.add(task4);
        TL1.add(task5);
        TL1.add(task6);
        TL1.add(task7);
        TL1.add(task8);
        TL1.add(task9);
        TL1.add(task10);
    
        return TL1;
    }
    
    //Testing sort by dates
    @Test
    public void testSort() throws Exception{
        initialize();
        Object[] para = new Object[1];
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("2476973463/999999/20155265");
        Date date2 = dateFormat.parse("22/09/2015");
        Date date3 = dateFormat.parse("21/09/2015");
        Date date4 = dateFormat.parse("-1999/-791/0");

        Task task1 = new Task("dummy1", "test", date, 1, "selfie", 1);
        Task task2 = new Task("dummy2", "test", date2, 1, "hashtags", 1);
        Task task3 = new Task("dummy3", "test", date3, 1, "merlini", 1);
        Task task4 = new Task("dummy4", "test", date4, 1, "1 shot", 1);
        
        TaskList TL1 = new TaskList();
        TL1.add(task1);
        TL1.add(task2);
        TL1.add(task3);
        TL1.add(task4);
        TaskList TL2 = new TaskList();
        TL2.add(task4);
        TL2.add(task3);
        TL2.add(task2);
        TL2.add(task1);
        
        /**
         * Expected Output:
         * All tasks are standard data participants 
         */
        TaskList TL3 = Logic.sortTaskListByDate(TL1);
        
        assertEquals(TL3.get(0).getName(), TL2.get(0).getName());
        assertEquals(TL3.get(1).getName(), TL2.get(1).getName());
        assertEquals(TL3.get(2).getName(), TL2.get(2).getName());
        assertEquals(TL3.get(3).getName(), TL2.get(3).getName());
    }
    
    //Testing dates more deeply
    //Test cases all have the same date but different priorities and names
    @Test
    public void testSortDeepByDate() throws Exception {
        initialize();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/09/2015");
        

        Task task1 = new Task("ZZZZ", "test", date, 1, "selfie", 1);
        Task task2 = new Task("~z{|}", "test", date, 9999999, "hashtags", 2);
        Task task3 = new Task("ABZ", "test", date, 1, "lolqop", 3);
        Task task4 = new Task("ZBXBZ", "test", date, 1, "alohadance", 4);
        Task task5 = new Task("ZZZZ", "test", date, 1, "selfie", 5);
        
        TaskList TL1 = new TaskList();
        TL1.add(task1);
        TL1.add(task2);
        TL1.add(task3);
        TL1.add(task4);
        TL1.add(task5);
        TaskList TL2 = new TaskList();
        TL2.add(task2);
        TL2.add(task3);
        TL2.add(task4);
        TL2.add(task1);
        TL2.add(task5);
        
        /**
         * Expected output: <2, 3, 4, 1>
         * All tasks are boundary data participants since all the dates are equal
         */
        TaskList TL3 = Logic.sortTaskListByDate(TL1);
        assertEquals(TL3.get(0), TL2.get(0));
        assertEquals(TL3.get(1), TL2.get(1));
        assertEquals(TL3.get(2), TL2.get(2));
        assertEquals(TL3.get(3), TL2.get(3));
        assertEquals(TL3.get(4), TL2.get(4));        
        
    }
    
    //Testing sort by priority
    //Test cases all have different date, priority, name and ID
    @Test
    public void testSortDeepByPriority() throws Exception {
        initialize();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/09/2015");
        Date date2 = dateFormat.parse("23/01/2015");
        Date date3 = dateFormat.parse("04/04/2004");
        Date date4 = dateFormat.parse("22/09/2015");

        Task task1 = new Task("ZZZZ", "test", date, 1, "selfie", 1);
        Task task2 = new Task("AAAAA", "test", date2, 2, "hashtags", 2);
        Task task3 = new Task("ABZ", "test", date3, 3, "lolqop", 1);
        Task task4 = new Task("ZBXBZ", "test", date4, 3, "alohadance", 4);
        Task task5 = new Task("AAAAA", "test", date2, 2, "n0tail", 5);
        
        
        TaskList TL1 = new TaskList();
        TL1.add(task1);
        TL1.add(task2);
        TL1.add(task3);
        TL1.add(task4);
        TL1.add(task5);
        TaskList TL2 = new TaskList();
        TL2.add(task3);
        TL2.add(task4);
        TL2.add(task2);
        TL2.add(task5);
        TL2.add(task1);
        
        /**
         * Expected output: <3, 4, 2, 5, 1>
         * Task 2 and 5, as well as 3 and 4, are all boundary data participants
         * since their priorities are the same
         * Task 1, (2, 5), (3, 4) are standard participants as they all have different
         * priorities
         */
        
        TaskList TL3 = Logic.sortTaskListByPriority(TL1);
        assertEquals(TL3.get(0), TL2.get(0));
        assertEquals(TL3.get(1), TL2.get(1));
        assertEquals(TL3.get(2), TL2.get(2));
        assertEquals(TL3.get(3), TL2.get(3));
        assertEquals(TL3.get(4), TL2.get(4));
    }
    
    //Tests search by tags
    //Test cases might have same tag string but different upper/lower cases, as well as substring containing the tag
    @Test 
    public void testSearchTags() throws Exception{
        initialize();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/09/2015"); 
        Task task1 = new Task("dummy1", "test", date, 1, "Can you work", 1);
        Task task2 = new Task("dummy2", "test", date, 2, "work hello world", 2);
        Task task3 = new Task("dummy3", "test", date, 1, "workload", 3);
        Task task4 = new Task("dummy4", "test", date, 1, "hello", 4);
        Task task5 = new Task("dummy5", "test", date, 1, "wOrK", 5);
        
        TaskList TL1 = new TaskList();
        TaskList Search = new TaskList();
        TL1.add(task1);
        TL1.add(task2);
        TL1.add(task3);
        TL1.add(task4);
        TL1.add(task5);
        TaskList TL2 = new TaskList();
        TL2.add(task1);
        TL2.add(task2);
        TL2.add(task5);
        
        /**
         * Expected Output:
         * task1, 2 and 4 are standard test case in the expected bound
         * task3 is a boundary case where searched result is part of the substring of the word searched
         * task5 is a boundary case where the letters have different cases
         */
        
        Search = Logic.searchTaskByTags(TL1, "work");
        assertEquals(Search.get(0), TL2.get(0));
        assertEquals(Search.get(1), TL2.get(1));
        assertEquals(Search.get(2), TL2.get(2));
  
    }
    
    //Test search for tags, name and description
    //Test cases include word in name, description and tags in different cases with a combination of all if possible
    @Test 
    public void testSearchAll() throws Exception{
        initialize();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/09/2015"); 
        Task task1 = new Task("Work to do for CS2103T", "Need to finish homework on demo video", date, 1, "work", 1);
        Task task2 = new Task("CS2103T", "Need to integrate components in homework", date, 2, "workload", 2);
        Task task3 = new Task("CS2101", "Have some wORk to finish", date, 1, "workload", 3);
        Task task4 = new Task("LAJ2201 WORK", "", date, 1, "", 4);
        Task task5 = new Task("CS3230", "have to finish homework", date, 1, "work", 5);
        
        TaskList TL1 = new TaskList();
        TaskList Search = new TaskList();
        TL1.add(task1);
        TL1.add(task2);
        TL1.add(task3);
        TL1.add(task4);
        TL1.add(task5);
        TaskList TL2 = new TaskList();
        TL2.add(task1);
        TL2.add(task5);
        
        /**
         * Expected Output: <1, 5>
         * Task
         */
        
        Search = Logic.searchAll(TL1, "fiNIsH hOMeWORk");
        assertEquals(Search.get(0), TL2.get(0));
        assertEquals(Search.get(1), TL2.get(1));
    }
    
    //This test determines whether the order of priority search is correct
    @Test
    public void testSearchPriority() throws Exception{
        initialize();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/09/2015"); 
        Task task1 = new Task("Work to do for CS2103T", "Need to finish homework on demo video", date, 1, "work", 1);
        Task task2 = new Task("CS2103T", "Need to integrate components", date, -1, "workload", 2);
        Task task3 = new Task("CS2101", "Have some wORk to do for CS2101", date, 0, "workload", 3);
        Task task4 = new Task("LAJ2201 WORK", "", date, 322, "", 4);
        Task task5 = new Task("CS3230", "have to finish homework", date, 323, "work", 5);
        
        TaskList TL1 = new TaskList();
        TaskList Search = new TaskList();
        TL1.add(task1);
        TL1.add(task2);
        TL1.add(task3);
        TL1.add(task4);
        TL1.add(task5);
        TaskList TL2 = new TaskList();
        TL2.add(task4);
        TL2.add(task5);
        
        /** Expected Output:
         *  First 3 tasks are boundary test cases for 'negative value' partition
         *  Last 2 tasks are boundary test cases for 'positive value' partition
         */
        
        Search = Logic.searchPriority(TL1, 3);
        assertEquals(Search.get(0), TL2.get(0));
        assertEquals(Search.get(1), TL2.get(1));
        
    }
    
    //Tests whether splitting the task list according to tentative tasks
    //is in order
    @Test
    public void testSplitTentative() throws Exception {
        initialize();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/09/2015"); 
        Date date2 = dateFormat.parse("20/12/9999");
        Task task1 = new Task("HELLO WORLD", "Need to finish homework on demo video", null, 1, "work", 1);
        Task task2 = new Task("SILHFGYUIJ", "Need to integrate components", date, 2, "workload", 2);
        Task task3 = new Task("CS2101", "Have some wORk to do for CS2101", null, 5, "workload", 3);
        Task task4 = new Task("LAJ2201 WORK", "", null, 1, "", 4);
        Task task5 = new Task("CS3230", "have to finish homework", date, 3, "work", 5);
        
        TaskList TL1 = new TaskList();
        TaskList SearchTent = new TaskList();
        TaskList SearchConf = new TaskList();
        TL1.add(task1);
        TL1.add(task2);
        TL1.add(task3);
        TL1.add(task4);
        TL1.add(task5);
        TaskList TL2 = new TaskList();
        TaskList TL3 = new TaskList();
        TL2.add(task1);
        TL2.add(task3);
        TL2.add(task4);
        TL3.add(task2);
        TL3.add(task5);
        
        /**
         * Expected Output: <1, 3, 4> and <2, 5>
         * All tasks are standard data participants
         */
        
        SearchTent = Logic.searchTentative(TL1);
        SearchConf = Logic.searchConfirmed(TL1);
        assertEquals(SearchTent.get(0), TL2.get(0));
        assertEquals(SearchTent.get(1), TL2.get(1));
        assertEquals(SearchTent.get(2), TL2.get(2));
        assertEquals(SearchConf.get(0), TL3.get(0));
        assertEquals(SearchConf.get(1), TL3.get(1));      
    }
    
    @Test
    public void testSplitDone() throws Exception {
        initialize();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/09/2015"); 
        Task task1 = new Task("HELLO WORLD", "Need to finish homework on demo video", null, 1, "work", 1);
        Task task2 = new Task("SILHFGYUIJ", "Need to integrate components", date, 2, "workload", 2);
        Task task3 = new Task("CS2101", "Have some wORk to do for CS2101", null, 5, "workload", 3);
        Task task4 = new Task("LAJ2201 WORK", "", null, 1, "", 4);
        Task task5 = new Task("CS3230", "have to finish homework", date, 3, "work", 5);
        
        task2.setDone();
        task4.setDone();
        task5.setDone();
        
        TaskList TL1 = new TaskList();
        TaskList SearchDone = new TaskList();
        TaskList SearchUndone = new TaskList();
        TL1.add(task1);
        TL1.add(task2);
        TL1.add(task3);
        TL1.add(task4);
        TL1.add(task5);
        TaskList TL2 = new TaskList();
        TaskList TL3 = new TaskList();
        TL2.add(task2);
        TL2.add(task4);
        TL2.add(task5);
        TL3.add(task1);
        TL3.add(task3);
        
        /**
         * Expected Output: <2, 4, 5> and <1, 3>
         * All tasks are standard data participants
         */
        
        SearchDone = Logic.searchDone(TL1);
        SearchUndone = Logic.searchNotDone(TL1);
        assertEquals(SearchDone.get(0), TL2.get(0));
        assertEquals(SearchDone.get(1), TL2.get(1));
        assertEquals(SearchDone.get(2), TL2.get(2));
        assertEquals(SearchUndone.get(0), TL3.get(0));
        assertEquals(SearchUndone.get(1), TL3.get(1));      
    }
    
    @Test
    public void testSplitAllTask() throws Exception {
        initialize();
        TaskList TL1 = initializeList();
        DisplayTaskList displayTest = Logic.splitAllTask(TL1);
        for (int i = 0; i < displayTest.size(); i++) {
            if (displayTest.get(i).getShownDate() != null) {
                System.out.println(displayTest.get(i).getParent().getID() + " " + displayTest.get(i).getShownDate().getDate());
            } else {
                System.out.println(displayTest.get(i).getParent().getID());
            }
        }
        assertEquals(displayTest.size(), 32);
        
    }
}
