package planner;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.junit.Test;

public class LogicTest {
    
    private static Task task1;
    private static Task task2;
    private static Task task3;
    private static Task task4;
    private static Task task5;
    private static Task task6;
    private static Task task7;
    private static Task task8;
    private static Task task9;
    private static Task task10;
    private static DisplayTask dt1;
    private static DisplayTask dt2;
    private static DisplayTask dt3;
    private static DisplayTask dt4;
    private static DisplayTask dt5;
    private static DisplayTask dt6;
    private static DisplayTask dt7;
    private static DisplayTask dt8;
    private static DisplayTask dt9;
    private static DisplayTask dt10;
    private static DisplayTask dt11;
    private static DisplayTask dt12;
    private static DisplayTask dt13;
    private static DisplayTask dt14;
    private static DisplayTask dt15;
    private static DisplayTask dt16;
    private static DisplayTask dt17;
    private static DisplayTask dt18;
    private static DisplayTask dt19;
    private static DisplayTask dt20;
    private static DisplayTask dt21;
    private static DisplayTask dt22;
    private static DisplayTask dt23;
    private static DisplayTask dt24;
    private static DisplayTask dt25;
    private static DisplayTask dt26;
    private static DisplayTask dt27;
    private static DisplayTask dt28;
    private static DisplayTask dt29;
    private static DisplayTask dt30;
    private static DisplayTask dt31;
    private static DisplayTask dt32;
    
    private static DisplayTaskList list1;
    private static DisplayTaskList list2;
    private static DisplayTaskList list3;
    private static DisplayTaskList list4;
    private static DisplayTaskList list5;
    private static DisplayTaskList list6;
    private static DisplayTaskList list7;
    private static DisplayTaskList list8;
    private static DisplayTaskList list9;
    private static DisplayTaskList list10;
    private static DisplayTaskList list11;
    private static DisplayTaskList list12;
    private static DisplayTaskList list13;
    private static DisplayTaskList list14;
    private static DisplayTaskList list15;
    private static DisplayTaskList list16;
    private static DisplayTaskList list17;
    private static DisplayTaskList list18;
    private static DisplayTaskList list19;
    private static DisplayTaskList list20;
    private static DisplayTaskList list21;
    private static DisplayTaskList list22;
    private static DisplayTaskList list23;
    private static DisplayTaskList list24;

    private TaskList initializeList() throws Exception {
        Calendar calFirst = Calendar.getInstance();
        calFirst.set(2015, 5, 3, 0, 0, 0);
        
        Calendar calSecond = Calendar.getInstance();
        calSecond.set(2015, 5, 8, 23, 59, 0);
        
        Calendar calThird = Calendar.getInstance();
        calThird.set(2015, 5, 3, 23, 59, 0);
        
        Calendar calFourth = Calendar.getInstance();
        calFourth.set(2014, 5, 10, 23, 59, 0);

        Calendar calOverDue = Calendar.getInstance();
        calOverDue.set(2015, 2, 20, 23, 59, 0);
        
        Calendar calOverDue1 = Calendar.getInstance();
        calOverDue1.set(2015, 3, 3, 0, 0, 0);
        
        Calendar calOverDue2 = Calendar.getInstance();
        calOverDue2.set(2015, 2, 23, 23, 59, 0);
        
        Calendar calZero = Calendar.getInstance();
        calZero.set(2015, 8, 23, 0, 0, 0);
        
        task1 = new Task("THIS IS A DUEDATE TASK", "NOTHING", calZero.getTime(), 1, "work", 1);
        task2 = new Task("THIS IS A TIMEDTASK", "SPREAD THROUGHOUT SEVERAL DAYS", calFirst.getTime(), 2, "workload", 2);
        task3 = new Task("THIS IS ANOTHER TIMEDTASK", "WITHIN ONE DAY", calFirst.getTime(), 2, "workload", 3);
        task4 = new Task("THIS IS AN OVERDUED TASK", "DUEDATE TASK", calOverDue.getTime(), 4, "nothing", 4);
        task5 = new Task("THIS IS AN OVERDUED TIMED TASK", "STILL ONGOING", calOverDue.getTime(), 3, "this is a tag", 5);
        task6 = new Task("THIS IS AN OVERDUED TIMED TASK", "LONG PAST", calOverDue.getTime(), 5, "tags cannot be searched can it", 6);
        task7 = new Task("THIS IS A FLOATING TASK", "NULL DATE", null, 2, "can you find this", 7);
        task8 = new Task("THIS IS A FLOATING TASK", "NULL DATE", null, 2, "these are tags", 8);
        task9 = new Task("THIS IS A FLOATING TASK", "HIGHEST PRIORITY", null, 5, "can you dig it", 9);
        task10 = new Task("THIS IS A RANDOM DUEDATE TASK", "", calFourth.getTime(), 4, "", 10);
        
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
    
    private void initializeDisplayList() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.set(2015, 8, 23, 0, 0, 0);
        
        dt1 = new DisplayTask(0, cal.getTime(), task1.getDueDate(), null, task1);
        
        cal.set(2015, 5, 3, 0, 0, 0);
        dt2 = new DisplayTask(1, cal.getTime(), task2.getDueDate(), null, task2);
        cal.add(Calendar.DATE, 1);
        dt3 = new DisplayTask(2, cal.getTime(), null, task2.getEndDate(), task2);
        cal.add(Calendar.DATE, 1);
        dt4 = new DisplayTask(3, cal.getTime(), null, task2.getEndDate(), task2);
        cal.add(Calendar.DATE, 1);
        dt5 = new DisplayTask(4, cal.getTime(), null, task2.getEndDate(), task2);
        cal.add(Calendar.DATE, 1);
        dt6 = new DisplayTask(5, cal.getTime(), null, task2.getEndDate(), task2);
        cal.add(Calendar.DATE, 1);
        dt7 = new DisplayTask(6, cal.getTime(), null, task2.getEndDate(), task2);
        
        cal.set(2015, 5, 3, 0, 0, 0);
        dt8 = new DisplayTask(7, cal.getTime(), task3.getDueDate(), task3.getEndDate(), task3);
        
        cal.set(2015, 2, 20, 0, 0, 0);
        dt9 = new DisplayTask(8, cal.getTime(), task4.getDueDate(), null, task4);
        
        dt10 = new DisplayTask(9, cal.getTime(), task5.getDueDate(), null, task5);
        cal.add(Calendar.DATE, 1);
        dt11 = new DisplayTask(10, cal.getTime(), null, task5.getEndDate(), task5);
        cal.add(Calendar.DATE, 1);
        dt12 = new DisplayTask(11, cal.getTime(), null, task5.getEndDate(), task5);
        cal.add(Calendar.DATE, 1);
        dt13 = new DisplayTask(12, cal.getTime(), null, task5.getEndDate(), task5);
        cal.add(Calendar.DATE, 1);
        dt14 = new DisplayTask(13, cal.getTime(), null, task5.getEndDate(), task5);
        cal.add(Calendar.DATE, 1);
        dt15 = new DisplayTask(14, cal.getTime(), null, task5.getEndDate(), task5);
        cal.add(Calendar.DATE, 1);
        dt16 = new DisplayTask(15, cal.getTime(), null, task5.getEndDate(), task5);
        cal.add(Calendar.DATE, 1);
        dt17 = new DisplayTask(16, cal.getTime(), null, task5.getEndDate(), task5);
        cal.add(Calendar.DATE, 1);
        dt18 = new DisplayTask(17, cal.getTime(), null, task5.getEndDate(), task5);
        cal.add(Calendar.DATE, 1);
        dt19 = new DisplayTask(18, cal.getTime(), null, task5.getEndDate(), task5);
        cal.add(Calendar.DATE, 1);
        dt20 = new DisplayTask(19, cal.getTime(), null, task5.getEndDate(), task5);
        cal.add(Calendar.DATE, 1);
        dt21 = new DisplayTask(20, cal.getTime(), null, task5.getEndDate(), task5);
        cal.add(Calendar.DATE, 1);
        dt22 = new DisplayTask(21, cal.getTime(), null, task5.getEndDate(), task5);
        cal.add(Calendar.DATE, 1);
        dt23 = new DisplayTask(22, cal.getTime(), null, task5.getEndDate(), task5);
        cal.add(Calendar.DATE, 1);
        dt24 = new DisplayTask(23, cal.getTime(), null, task5.getEndDate(), task5);
        
        cal.set(2015, 2, 20, 0, 0, 0);
        dt25 = new DisplayTask(24, cal.getTime(), task6.getDueDate(), null, task6);
        cal.add(Calendar.DATE, 1);
        dt26 = new DisplayTask(25, cal.getTime(), null, task6.getEndDate(), task6);
        cal.add(Calendar.DATE, 1);
        dt27 = new DisplayTask(26, cal.getTime(), null, task6.getEndDate(), task6);
        cal.add(Calendar.DATE, 1);
        dt28 = new DisplayTask(27, cal.getTime(), null, task6.getEndDate(), task6);
        
        dt29 = new DisplayTask(28, null, null, null, task7);
        dt30 = new DisplayTask(29, null, null, null, task8);
        dt31 = new DisplayTask(30, null, null, null, task9);
        
        cal.set(2014, 5, 10, 0, 0, 0);
        dt32 = new DisplayTask(31, cal.getTime(), task10.getDueDate(), null, task10);
        
    }
    
    private void initializeTreeLists() throws Exception{
        list1 = new DisplayTaskList();
        list2 = new DisplayTaskList();
        list3 = new DisplayTaskList();
        list4 = new DisplayTaskList();
        list5 = new DisplayTaskList();
        list6 = new DisplayTaskList();
        list7 = new DisplayTaskList();
        list8 = new DisplayTaskList();
        list9 = new DisplayTaskList();
        list10 = new DisplayTaskList();
        list11 = new DisplayTaskList();
        list12 = new DisplayTaskList();
        list13 = new DisplayTaskList();
        list14 = new DisplayTaskList();
        list15 = new DisplayTaskList();
        list16 = new DisplayTaskList();
        list17 = new DisplayTaskList();
        list18 = new DisplayTaskList();
        list19 = new DisplayTaskList();
        list20 = new DisplayTaskList();
        list21 = new DisplayTaskList();
        list22 = new DisplayTaskList();
        list23 = new DisplayTaskList();
        list24 = new DisplayTaskList();
    }
    
    //Tests search by tags
    //Test cases might have same tag string but different upper/lower cases, as well as substring containing the tag
    @Test 
    public void testSearchTags() throws Exception{
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
        TL2.add(task3);
        TL2.add(task5);
        
        /**
         * Expected Output:
         * task1, 2 and 4 are standard test case in the expected bound
         * task3 is a boundary case where searched result is part of the substring of the word searched
         * task5 is a boundary case where the letters have different cases
         */
        
        Search = Logic.searchTag(TL1, "work");
        assertEquals(Search.get(0), TL2.get(0));
        assertEquals(Search.get(1), TL2.get(1));
        assertEquals(Search.get(2), TL2.get(2));
        assertEquals(Search.get(3), TL2.get(3));
  
    }
    
    //Test search for tags, name and description
    //Test cases include word in name, description and tags in different cases with a combination of all if possible
    @Test 
    public void testSearchDesc() throws Exception{
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
        
        Search = Logic.searchDescription(TL1, "fiNIsH hOMeWORk");
        assertEquals(Search.get(0), TL2.get(0));
        assertEquals(Search.get(1), TL2.get(1));
    }
    
    //This test determines whether the order of priority search is correct
    @Test
    public void testSearchPriority() throws Exception{
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
        
        SearchTent = Logic.searchFloating(TL1);
        SearchConf = Logic.searchConfirmed(TL1);
        assertEquals(SearchTent.get(0), TL2.get(0));
        assertEquals(SearchTent.get(1), TL2.get(1));
        assertEquals(SearchTent.get(2), TL2.get(2));
        assertEquals(SearchConf.get(0), TL3.get(0));
        assertEquals(SearchConf.get(1), TL3.get(1));      
    }
    
    @Test
    public void testSplitDone() throws Exception {
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

        TaskList TL1 = initializeList();
        initializeDisplayList();
        DisplayTaskList displayTest = Logic.splitAllTask(TL1);

        DisplayTaskList splitTest = new DisplayTaskList();
        splitTest.add(dt1);
        splitTest.add(dt2);
        splitTest.add(dt3);
        splitTest.add(dt4);
        splitTest.add(dt5);
        splitTest.add(dt6);
        splitTest.add(dt7);
        splitTest.add(dt8);
        splitTest.add(dt9);
        splitTest.add(dt10);
        splitTest.add(dt11);
        splitTest.add(dt12);
        splitTest.add(dt13);
        splitTest.add(dt14);
        splitTest.add(dt15);
        splitTest.add(dt16);
        splitTest.add(dt17);
        splitTest.add(dt18);
        splitTest.add(dt19);
        splitTest.add(dt20);
        splitTest.add(dt21);
        splitTest.add(dt22);
        splitTest.add(dt23);
        splitTest.add(dt24);
        splitTest.add(dt25);
        splitTest.add(dt26);
        splitTest.add(dt27);
        splitTest.add(dt28);
        splitTest.add(dt29);
        splitTest.add(dt30);
        splitTest.add(dt31);
        splitTest.add(dt32);
        
        assertEquals(displayTest, splitTest);
        
    }
    
    @Test
    public void testSplitWithDone() throws Exception{
        TaskList TL1 = initializeList();
        TL1.get(0).setDone();
        TL1.get(2).setDone();
        TL1.get(4).setDone();
        initializeDisplayList();
        DisplayTaskList displayTest = Logic.splitAllTask(TL1);
        
        DisplayTaskList splitTest = new DisplayTaskList();
        Calendar cal = Calendar.getInstance();
        cal.setTime(TL1.get(0).getDateCompleted());
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 0, 0, 0);
        
        DisplayTask done1 = new DisplayTask(0, cal.getTime(), null, TL1.get(0).getDateCompleted(), TL1.get(0));
        
        cal.setTime(TL1.get(2).getDateCompleted());
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 0, 0, 0);
        DisplayTask done3 = new DisplayTask(7, cal.getTime(), null, TL1.get(2).getDateCompleted(), TL1.get(2));
        
        cal.setTime(TL1.get(4).getDateCompleted());
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 0, 0, 0);
        DisplayTask done5 = new DisplayTask(9, cal.getTime(), null, TL1.get(4).getDateCompleted(), TL1.get(4));
        
        splitTest.add(done1);
        splitTest.add(dt2);
        splitTest.add(dt3);
        splitTest.add(dt4);
        splitTest.add(dt5);
        splitTest.add(dt6);
        splitTest.add(dt7);
        splitTest.add(done3);
        splitTest.add(dt9);
        splitTest.add(done5);

        dt25.setID(10);
        dt26.setID(11);
        dt27.setID(12);
        dt28.setID(13);
        dt29.setID(14);
        dt30.setID(15);
        dt31.setID(16);
        dt32.setID(17);
        splitTest.add(dt25);
        splitTest.add(dt26);
        splitTest.add(dt27);
        splitTest.add(dt28);
        splitTest.add(dt29);
        splitTest.add(dt30);
        splitTest.add(dt31);
        splitTest.add(dt32);
        
        assertEquals(displayTest, splitTest);
       
    }
    
    @Test
    public void testConvertToDateTree() throws Exception {
        TaskList TL1 = initializeList();
        initializeDisplayList();
        initializeTreeLists();
        DisplayTaskList displayTest = Logic.splitAllTask(TL1);
        TreeMap <Date, DisplayTaskList> map = Logic.convertToTreeMapWithDate(displayTest);

        list1.add(dt1);
        list2.add(dt2);
        list2.add(dt8);
        list3.add(dt3);
        list4.add(dt4);
        list5.add(dt5);
        list6.add(dt6);
        list7.add(dt7);
        list8.add(dt9);
        list8.add(dt10);
        list8.add(dt25);
        list9.add(dt11);
        list9.add(dt26);
        list10.add(dt12);
        list10.add(dt27);
        list11.add(dt13);
        list11.add(dt28);
        list12.add(dt14);
        list13.add(dt15);
        list14.add(dt16);
        list15.add(dt17);
        list16.add(dt18);
        list17.add(dt19);
        list18.add(dt20);
        list19.add(dt21);
        list20.add(dt22);
        list21.add(dt23);
        list22.add(dt24);
        list23.add(dt29);
        list23.add(dt30);
        list23.add(dt31);
        list24.add(dt32);
        
        assertEquals(map.get(dt1.getShownDate()), list1);
        assertEquals(map.get(dt3.getShownDate()), list3);
        assertEquals(map.get(dt4.getShownDate()), list4);
        assertEquals(map.get(dt5.getShownDate()), list5);
        assertEquals(map.get(dt6.getShownDate()), list6);
        assertEquals(map.get(dt7.getShownDate()), list7);
        assertEquals(map.get(dt9.getShownDate()), list8);
        assertEquals(map.get(dt11.getShownDate()), list9);
        assertEquals(map.get(dt12.getShownDate()), list10);
        assertEquals(map.get(dt13.getShownDate()), list11);
        assertEquals(map.get(dt14.getShownDate()), list12);
        assertEquals(map.get(dt15.getShownDate()), list13);
        assertEquals(map.get(dt16.getShownDate()), list14);
        assertEquals(map.get(dt17.getShownDate()), list15);
        assertEquals(map.get(dt18.getShownDate()), list16);
        assertEquals(map.get(dt19.getShownDate()), list17);
        assertEquals(map.get(dt20.getShownDate()), list18);
        assertEquals(map.get(dt21.getShownDate()), list19);
        assertEquals(map.get(dt22.getShownDate()), list20);
        assertEquals(map.get(dt23.getShownDate()), list21);
        assertEquals(map.get(dt24.getShownDate()), list22);
        assertEquals(map.get(dt29.getShownDate()), list23);
        assertEquals(map.get(dt32.getShownDate()), list24);
        
    }
    
    @Test
    public void testConvertToFloatingTree() throws Exception {

        TaskList TL1 = initializeList();
        TaskList TL = Logic.searchFloating(TL1);
        DisplayTaskList displayTest = Logic.splitAllTask(TL);
        TreeMap <Integer, DisplayTaskList> map = Logic.convertToTreeMapWithPriority(displayTest);
        
        initializeDisplayList();
        initializeTreeLists();
        
        list1.add(dt29);
        list1.get(0).setID(0);
        list1.add(dt30);
        list1.get(1).setID(1);
        list2.add(dt31);
        list2.get(0).setID(2);
        
        assertEquals(map.get(dt29.getParent().getPriority()), list1);
        assertEquals(map.get(dt31.getParent().getPriority()), list2);
    }
    
    @Test
    public void sortDateTree() throws Exception {

        TaskList TL1 = initializeList();
        initializeDisplayList();
        initializeTreeLists();
        DisplayTaskList displayTest = Logic.splitAllTask(TL1);
        TreeMap <Date, DisplayTaskList> map = Logic.convertToTreeMapWithDate(displayTest);
        TreeMap <Date, DisplayTaskList> sortedMap = SortLogic.sortTreeMapIntoSetMapByDate(map);
        
        list1.add(dt1);
        list2.add(dt2);
        list2.add(dt8);
        list3.add(dt3);
        list4.add(dt4);
        list5.add(dt5);
        list6.add(dt6);
        list7.add(dt7);
        list8.add(dt25);
        list8.add(dt9);
        list8.add(dt10);
        list9.add(dt26);
        list9.add(dt11);
        list10.add(dt27);
        list10.add(dt12);
        list11.add(dt28);
        list11.add(dt13);
        list12.add(dt14);
        list13.add(dt15);
        list14.add(dt16);
        list15.add(dt17);
        list16.add(dt18);
        list17.add(dt19);
        list18.add(dt20);
        list19.add(dt21);
        list20.add(dt22);
        list21.add(dt23);
        list22.add(dt24);
        list23.add(dt31);
        list23.add(dt29);
        list23.add(dt30);
        list24.add(dt32);
        
        assertEquals(sortedMap.get(dt1.getShownDate()), list1);
        assertEquals(sortedMap.get(dt2.getShownDate()), list2);
        assertEquals(sortedMap.get(dt3.getShownDate()), list3);
        assertEquals(sortedMap.get(dt4.getShownDate()), list4);
        assertEquals(sortedMap.get(dt5.getShownDate()), list5);
        assertEquals(sortedMap.get(dt6.getShownDate()), list6);
        assertEquals(sortedMap.get(dt7.getShownDate()), list7);
        assertEquals(sortedMap.get(dt9.getShownDate()), list8);
        assertEquals(sortedMap.get(dt11.getShownDate()), list9);
        assertEquals(sortedMap.get(dt12.getShownDate()), list10);
        assertEquals(sortedMap.get(dt13.getShownDate()), list11);
        assertEquals(sortedMap.get(dt14.getShownDate()), list12);
        assertEquals(sortedMap.get(dt15.getShownDate()), list13);
        assertEquals(sortedMap.get(dt16.getShownDate()), list14);
        assertEquals(sortedMap.get(dt17.getShownDate()), list15);
        assertEquals(sortedMap.get(dt18.getShownDate()), list16);
        assertEquals(sortedMap.get(dt19.getShownDate()), list17);
        assertEquals(sortedMap.get(dt20.getShownDate()), list18);
        assertEquals(sortedMap.get(dt21.getShownDate()), list19);
        assertEquals(sortedMap.get(dt22.getShownDate()), list20);
        assertEquals(sortedMap.get(dt23.getShownDate()), list21);
        assertEquals(sortedMap.get(dt24.getShownDate()), list22);
        assertEquals(sortedMap.get(dt29.getShownDate()), list23);
        assertEquals(sortedMap.get(dt32.getShownDate()), list24);
      
    }
    
    @Test
    public void sortPriorityTree() throws Exception {
        
    }
}
