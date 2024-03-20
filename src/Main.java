import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        /* Приветствую). Честно говоря, у меня есть стойкое ощущение, что я не совсем понял задание
           Прошу меня извинить, если я понаписал что-то странное и несуразное.
           Но все же, рискну сдать такую работу. Огромная просьба, если все совсем не то и не так, направь меня
           ,пожалуйста, в нужное русло.
            И почему-то у меня не получилось из Idea запушить в GitHub. Как будто нет измененых файлов для commit
        */
        System.out.println("Поехали!");
        Task task1 = new Task("Вызвать такси","Вызвать грузовое такси");
        Task task2 = new Task("Собрать коробку","Собрать коробку для книг");
        Epic epic1 = new Epic("Очень важный эпик","Очень важный эпик");

        Epic epic2 = new Epic("Переезд","Переезд на дачу");
        ArrayList<SubTask> subTasksList1 = new ArrayList<>();
        SubTask subTask1 =new SubTask("Упаковать вещи","Упаоквать вещи в коробку и перемотать скотчем");
        subTask1.setEpic(epic2);
        subTasksList1.add(subTask1);
        SubTask subTask2 =new SubTask("Собрать тарелки","Собрать тарелки в стопки");
        subTask2.setEpic(epic2);
        subTasksList1.add(subTask2);
        epic2.setSubTasks(subTasksList1);


        Epic epic3= new Epic("Сделать задание 4 спринта","Сделать задание 4 спринта");
        ArrayList<SubTask> subTasksList2 = new ArrayList<>();
        SubTask subTask3 = new SubTask("Понять, что нужно","Понять, как должна работать программа");
        subTasksList2.add(subTask3);
        subTask3.setEpic(epic3);
        epic3.setSubTasks(subTasksList2);

        TaskManager taskManager = new TaskManager();
        taskManager.add(task1);
        taskManager.add(task2);
        taskManager.add(epic1);
        taskManager.add(epic2);
        taskManager.add(epic3);
        //Получение списка всех задач
        for( Task task :taskManager.getTasksList()){
            System.out.println(task);
        }

        for( Epic epic :taskManager.getEpicsList()){
            System.out.println(epic);
        }
        for( SubTask subTask :taskManager.getSubTasksList()){
            System.out.println(subTask);
        }

        System.out.println("Получение всех подзадач определенного эпика");
        for(SubTask subTask : taskManager.getSubTasksByEpic(epic3)){
            System.out.println(subTask);
        }

        for(SubTask subTask : taskManager.getSubTasksByEpic(epic2)){
            System.out.println(subTask);
        }
        System.out.println("Смена статуса у подзадач");
        for(SubTask subTask : taskManager.getSubTasksByEpic(epic2)){
            subTask.setStatus(TaskStatus.IN_PROGRESS);
            System.out.println("Epic: " + subTask.getEpic());
        }

        System.out.println("Смена статуса у подзадач (IN_PROGRESS/NEW)");
        for(SubTask subTask : taskManager.getSubTasksByEpic(epic2)){
            subTask.setStatus(TaskStatus.DONE);
            System.out.println("Epic: " + subTask.getEpic());
        }


    }

}
