package planner;

import static org.junit.Assert.*;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class LogicTest {
    private Logic targetClass;
    
    public void initialize(){
        targetClass = new Logic();
    }
    
    //Testing sort by dates
    //All test cases are simple ones with different dates
    @Test
    public void testSort() throws Exception{
        initialize();
        Object[] para = new Object[1];
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/09/2015");
        Date date2 = dateFormat.parse("22/09/2015");
        Date date3 = dateFormat.parse("30/08/2015");
        Date date4 = dateFormat.parse("10/08/2013");
        /*
        long time = date.getTime();
        Timestamp dueDate = new Timestamp(time);
        Date now = new Date();
        Timestamp dateCreated = new Timestamp(now.getTime());
        */
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
        
        Logic.sortTaskListByDate(TL1);
        assertEquals(TL1.get(0).getName(), TL2.get(0).getName());
        assertEquals(TL1.get(1).getName(), TL2.get(1).getName());
        assertEquals(TL1.get(2).getName(), TL2.get(2).getName());
        assertEquals(TL1.get(3).getName(), TL2.get(3).getName());
    }
    
    //Testing dates more deeply
    //Test cases all have the same date but different priorities and names
    @Test
    public void testSortDeepByDate() throws Exception {
        initialize();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/09/2015");
        

        Task task1 = new Task("ZZZZ", "test", date, 1, "selfie", 1);
        Task task2 = new Task("AAAAA", "test", date, 2, "hashtags", 2);
        Task task3 = new Task("ABZ", "test", date, 1, "lolqop", 3);
        Task task4 = new Task("ZBXBZ", "test", date, 1, "alohadance", 4);
        
        TaskList TL1 = new TaskList();
        TL1.add(task1);
        TL1.add(task2);
        TL1.add(task3);
        TL1.add(task4);
        TaskList TL2 = new TaskList();
        TL2.add(task3);
        TL2.add(task4);
        TL2.add(task1);
        TL2.add(task2);
        
        /**
         * Expected output:
         * task3 starting with A 
         * task4 starting with ZB
         * task1 starting with ZZ
         * task2 with all As since it is lower in priority compared to the others
         */
        
        Logic.sortTaskListByDate(TL1);
        assertEquals(TL1.get(0), TL2.get(0));
        assertEquals(TL1.get(1), TL2.get(1));
        assertEquals(TL1.get(2), TL2.get(2));
        assertEquals(TL1.get(3), TL2.get(3));
        
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
        TL2.add(task1);
        TL2.add(task2);
        TL2.add(task5);
        TL2.add(task3);
        TL2.add(task4);
        
        /**
         * Expected output: 
         * task1 with priority 1
         * task2 with lower ID as compared to task5 with everything the same
         * task3 with same priority but lexicographically smaller name compared to task4
         */
        
        Logic.sortTaskListByPriority(TL1);
        assertEquals(TL1.get(0), TL2.get(0));
        assertEquals(TL1.get(1), TL2.get(1));
        assertEquals(TL1.get(2), TL2.get(2));
        assertEquals(TL1.get(3), TL2.get(3));
        assertEquals(TL1.get(4), TL2.get(4));
    }
    
    //Tests search by tags
    //Test cases might have same tag string but different upper/lower cases, as well as substring containing the tag
    @Test 
    public void testSearchTags() throws Exception{
        initialize();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/09/2015"); 
        Task task1 = new Task("dummy1", "test", date, 1, "Work", 1);
        Task task2 = new Task("dummy2", "test", date, 2, "work", 2);
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
        
        Logic.searchTaskByTags(TL1, Search, "woRK");
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
        Task task1 = new Task("Work to do for CS2103T", "Need to work on demo video", date, 1, "work", 1);
        Task task2 = new Task("CS2103T", "Need to integrate components", date, 2, "workload", 2);
        Task task3 = new Task("CS2101", "Have some wORk to do for CS2101", date, 1, "workload", 3);
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
        TL2.add(task3);
        TL2.add(task4);
        TL2.add(task5);
        
        Logic.searchAll(TL1, Search, "work");
        assertEquals(Search.get(0), TL2.get(0));
        assertEquals(Search.get(1), TL2.get(1));
        assertEquals(Search.get(2), TL2.get(2));
        assertEquals(Search.get(3), TL2.get(3));
    }
    
    //Test splitting by completion
    //Test cases are by mixing some incomplete with completed tasks
    @Test
    public void testSplitDone() throws Exception {
        initialize();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/09/2015"); 
        
        Task task1 = new Task("Work to do for CS2103T", "Need to work on demo video", null, 1, "work", 1);
        Task task2 = new Task("CS2103T", "Need to integrate components", date, 2, "workload", 2);
        Task task3 = new Task("CS2101", "Have some wORk to do for CS2101", null, 1, "workload", 3);
        Task task4 = new Task("LAJ2201 WORK", "", null, 1, "", 4);
        Task task5 = new Task("CS3230", "have to finish homework", date, 1, "work", 5);
        
        task2.setDone();
        task4.setDone();
        task5.setDone();
        
        TaskList TL1 = new TaskList();
        TaskList done = new TaskList();
        TaskList notDone = new TaskList();
        TL1.add(task1);
        TL1.add(task2);
        TL1.add(task3);
        TL1.add(task4);
        TL1.add(task5);
        TaskList TL2 = new TaskList();
        TL2.add(task2);
        TL2.add(task4);
        TL2.add(task5);
        TaskList TL3 = new TaskList();  
        TL3.add(task1);
        TL3.add(task3);
        
        Logic.splitTaskByDone(TL1, done, notDone);
        assertEquals(done.get(0), TL2.get(0));
        assertEquals(done.get(1), TL2.get(1));
        assertEquals(done.get(2), TL2.get(2));
        
        assertEquals(notDone.get(0), TL3.get(0));
        assertEquals(notDone.get(1), TL3.get(1));  
    }
    
    //Tests splitting by tentative
    //Test cases are by mixing some floating tasks with confirmed tasks
    @Test
    public void TestSplitTentative() throws Exception {
        initialize();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/09/2015"); 
        
        Task task1 = new Task("Work to do for CS2103T", "Need to work on demo video", null, 1, "work", 1);
        Task task2 = new Task("CS2103T", "Need to integrate components", date, 2, "workload", 2);
        Task task3 = new Task("CS2101", "Have some wORk to do for CS2101", null, 1, "workload", 3);
        Task task4 = new Task("LAJ2201 WORK", "", null, 1, "", 4);
        Task task5 = new Task("CS3230", "have to finish homework", date, 1, "work", 5);
        
        TaskList TL1 = new TaskList();
        TaskList confirmed = new TaskList();
        TaskList tentative = new TaskList();
        TL1.add(task1);
        TL1.add(task2);
        TL1.add(task3);
        TL1.add(task4);
        TL1.add(task5);
        TaskList TL2 = new TaskList();
        TL2.add(task2);
        TL2.add(task5);
        TaskList TL3 = new TaskList();  
        TL3.add(task1);
        TL3.add(task3);
        TL3.add(task4);
        
        Logic.splitTasksByTentative(TL1, confirmed, tentative);
        assertEquals(confirmed.get(0), TL2.get(0));
        assertEquals(confirmed.get(1), TL2.get(1));
        
        assertEquals(tentative.get(0), TL3.get(0));
        assertEquals(tentative.get(1), TL3.get(1));
        assertEquals(tentative.get(2), TL3.get(2));
    }
}
