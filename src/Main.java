import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import service.Managers;
import service.interfaces.TaskManager;
/*
* Приветсвую! Продолжаю традицию по описанию непоняток)
* В этот раз проблема с тестами. Часть тестов я сделал, а по части тестов, которые описаны в ТЗ, совсем не понимаю
* Тут наверное лучше задать вопрос наставнику или в чат, но напишу здесь, дабы понимать, а все остальное, может я
* тоже не верно понял). Если так плохо делать, то сообщи мне плиз.
* Итак:
    вот эти 2 теста, я не понимаю, как проверить в принципе. У нас же везде хранятся id, а не сами объекты.
    Или тут имеется ввиду, что я должен был в InMemoryTaskManager создасть(к примере) метод setEpic, и в нем проверить
        корректность переданного объекта(но я бы его все равно создал с параметром Epic.. Вобще не понял)
    *   проверьте, что объект Epic нельзя добавить в самого себя в виде подзадачи;
    *   проверьте, что объект Subtask нельзя сделать своим же эпиком;

    Тут я не понял, что значит не "конфликтует".. Это означает, что не должно быть 2 задач с одинаковым id?
    Но  getSeqId() же private, и наружу класса ее не отдаем.. Только внутри add..
     *проверьте, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера;
    Нужно сделать было equals по всем полям?
     *создайте тест, в котором проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер

* */
public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");

        TaskManager taskManager = Managers.getDefault();

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

        Epic epic3 = new Epic("Сделать задание 4 спринта", "Сделать задание 4 спринта");
        taskManager.addEpic(epic3);
        SubTask subTask3 = new SubTask("Понять, что нужно", "Понять, как должна работать программа");
        subTask3.setEpicId(epic3.getId());
        taskManager.addSubTask(subTask3);

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
        System.out.println("epic3");
        for (SubTask subTask : taskManager.getSubTasksByEpic(epic3.getId())) {
            System.out.println(subTask);
        }
        System.out.println("epic2");
        for (SubTask subTask : taskManager.getSubTasksByEpic(epic2.getId())) {
            System.out.println(subTask);
        }
        System.out.println("Смена статуса у подзадач");
        System.out.println("epic2");
        for (SubTask subTask : taskManager.getSubTasksByEpic(epic2.getId())) {
            subTask.setStatus(TaskStatus.IN_PROGRESS);
            System.out.println(subTask);
            taskManager.updateSubTask(subTask);
            System.out.println(epic2);
        }

        System.out.println("Смена статуса у подзадач (IN_PROGRESS/DONE)");
        System.out.println("epic2");
        for (SubTask subTask : taskManager.getSubTasksByEpic(epic2.getId())) {
            subTask.setStatus(TaskStatus.DONE);
            taskManager.updateSubTask(subTask);
            System.out.println("model.Epic: " + epic2);
        }

        printAllTasks(taskManager);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("********************");
        System.out.println("********************");
        System.out.println("Задачи:");
        for (Task task : manager.getTasksList()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getEpicsList()) {
            System.out.println(epic);

            for (Task task : manager.getSubTasksByEpic(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubTasksList()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
