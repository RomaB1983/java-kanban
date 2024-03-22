import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import service.TaskManager;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        /* Приветствую). Честно говоря, у меня есть стойкое ощущение, что я не совсем понял задание
           Прошу меня извинить, если я понаписал что-то странное и несуразное.
           Но все же, рискну сдать такую работу. Огромная просьба, если все совсем не то и не так, направь меня
           ,пожалуйста, в нужное русло.
            И почему-то у меня не получилось из Idea запушить в GitHub. Как будто нет измененых файлов для commit
        */
        /*
         Часть 2
         Спасибо большое за комментарии. Мне стало понятно. Постарался Учесть все замечания
         Единственно, меня смущает, что коллеги по курсе в мессенджере пишут, что нужно приводить типы у тасков, епиков.
         Но у меня это нигде не используется. Возможно опять что-то пошло не так) Туплю видимо.
         И еще, если можно подскажи пожалуйста.  в Условиях ТЗ есть такая фраза
         Также советуем применить знания о методах equals() и hashCode(),
         чтобы реализовать идентификацию задачи по её id.
         При этом две задачи с одинаковым id должны выглядеть для менеджера как одна и та же.
         И далее
         💡Эти методы нежелательно переопределять в наследниках. Ваша задача — подумать, почему.
         Правильно понимаю, нежелательно переопределять их потому, что id - атрибут класса model.Task? и не стоит в потомках
         менять поведение по инденификации по id?

         */
        System.out.println("Поехали!");

        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("Вызвать такси", "Вызвать грузовое такси");
        Task task2 = new Task("Собрать коробку", "Собрать коробку для книг");
        Epic epic1 = new Epic("Очень важный эпик", "Очень важный эпик");
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);

        Epic epic2 = new Epic("Переезд", "Переезд на дачу");
        taskManager.addEpic(epic2);

        SubTask subTask1 = new SubTask("Упаковать вещи", "Упаоквать вещи в коробку и перемотать скотчем");
        subTask1.setEpicId(epic2.getId());
        taskManager.addSubTask(subTask1);

        SubTask subTask2 = new SubTask("Собрать тарелки", "Собрать тарелки в стопки");
        subTask2.setEpicId(epic2.getId());
        taskManager.addSubTask(subTask2);

        ArrayList<Integer> subTasksList1 = new ArrayList<>();
        subTasksList1.add(subTask1.getId());
        subTasksList1.add(subTask2.getId());
        epic2.setSubTasksIds(subTasksList1);


        Epic epic3 = new Epic("Сделать задание 4 спринта", "Сделать задание 4 спринта");
        taskManager.addEpic(epic3);
        SubTask subTask3 = new SubTask("Понять, что нужно", "Понять, как должна работать программа");
        subTask3.setEpicId(epic3.getId());
        taskManager.addSubTask(subTask3);

        ArrayList<Integer> subTasksList2 = new ArrayList<>();
        subTasksList2.add(subTask3.getId());

        epic3.setSubTasksIds(subTasksList2);

        //Получение списка всех задач
        for (Task task : taskManager.getTasksList()) {
            System.out.println(task);
        }

        for (Epic epic : taskManager.getEpicsList()) {
            System.out.println(epic);
        }
        for (SubTask subTask : taskManager.getSubTasksList()) {
            System.out.println(subTask);
        }

        System.out.println("Получение всех подзадач определенного эпика");
        for (SubTask subTask : taskManager.getSubTasksByEpic(epic3.getId())) {
            System.out.println(subTask);
        }

        for (SubTask subTask : taskManager.getSubTasksByEpic(epic2.getId())) {
            System.out.println(subTask);
        }
        System.out.println("Смена статуса у подзадач");
        for (SubTask subTask : taskManager.getSubTasksByEpic(epic2.getId())) {
            subTask.setStatus(TaskStatus.IN_PROGRESS);
            System.out.println("subTask = " + subTask);
            taskManager.updateSubTask(subTask);
            System.out.println("model.Epic: " + epic2);
        }

        System.out.println("Смена статуса у подзадач (IN_PROGRESS/NEW)");
        for (SubTask subTask : taskManager.getSubTasksByEpic(epic2.getId())) {
            subTask.setStatus(TaskStatus.DONE);
            taskManager.updateSubTask(subTask);
            System.out.println("model.Epic: " + epic2);
        }


    }

}
