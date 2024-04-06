package service;

import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.interfaces.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    static TaskManager taskManager;
    static SubTask subTask1 = new SubTask("сабтаск1",
            "описание сабтаск1");
    static SubTask subTask2 = new SubTask("сабтаск2",
            "описание сабтаск2");
    static Task task1 = new Task("Вызвать такси", "Вызвать грузовое такси");
    static Epic epic1 = new Epic("Очень важный эпик", "Очень важный эпик");

    @BeforeEach
    void beforeEach() {
        taskManager = Managers.getDefault();
        taskManager.addTask(task1);
        taskManager.addEpic(epic1);

        subTask1.setStatus(TaskStatus.NEW);
        subTask1.setEpicId(epic1.getId());
        taskManager.addSubTask(subTask1);

        subTask2.setStatus(TaskStatus.NEW);
        subTask2.setEpicId(epic1.getId());
        taskManager.addSubTask(subTask2);
        taskManager.addSubTask(subTask2);
    }

    @Test
    void isPositiveFoundSubTaskById() {
        assertEquals(taskManager.getSubTask(subTask1.getId()), subTask1, "Subtask не найден по ID");
    }

    @Test
    void isPositiveFoundTaskById() {
        assertEquals(taskManager.getTask(task1.getId()), task1, "Task не найден по ID");
    }

    @Test
    void isPositiveFoundEpicById() {
        assertEquals(taskManager.getEpic(epic1.getId()), epic1, "Epic не найден по ID");
    }

    @Test
    void isShouldBeEqualsEpicAttrsBeforeAndAfterAddToManager() {
        Epic epic = new Epic("name", "description");
        String epicName = epic.getName();
        String epicDesc = epic.getDescription();
        TaskStatus epicStatus = epic.getStatus();
        taskManager.addEpic(epic);

        assertEquals(taskManager.getEpic(epic.getId()).getName(), epicName,
                "Наименование Epic не совпадает до добавления и после добавления в manager ");
        assertEquals(taskManager.getEpic(epic.getId()).getDescription(), epicDesc,
                "Описание Epic не совпадает до добавления и после добавления в manager ");
        assertEquals(taskManager.getEpic(epic.getId()).getStatus(), epicStatus,
                "Статус Epic не совпадает до добавления и после добавления в manager ");
    }

    @Test
    void isShouldBeEqualsTaskAttrsBeforeAndAfterAddToManager() {
        Task task = new Task("name", "description");
        String taskName = task.getName();
        String taskDesc = task.getDescription();
        TaskStatus taskStatus = task.getStatus();
        taskManager.addTask(task);

        assertEquals(taskManager.getTask(task.getId()).getName(), taskName,
                "Наименование Task не совпадает до добавления и после добавления в manager ");
        assertEquals(taskManager.getTask(task.getId()).getDescription(), taskDesc,
                "Описание Task не совпадает до добавления и после добавления в manager ");
        assertEquals(taskManager.getTask(task.getId()).getStatus(), taskStatus,
                "Статус Task не совпадает до добавления и после добавления в manager ");
    }

    @Test
    void isShouldBeEqualsSubTaskAttrsBeforeAndAfterAddToManager() {
        SubTask subTask = new SubTask("name", "description");
        String taskName = subTask.getName();
        String taskDesc = subTask.getDescription();
        Integer epicId = subTask.getEpicId();
        TaskStatus taskStatus = subTask.getStatus();

        taskManager.addSubTask(subTask);

        assertEquals(taskManager.getSubTask(subTask.getId()).getName(), taskName,
                "Наименование SubTask не совпадает до добавления и после добавления в manager ");
        assertEquals(taskManager.getSubTask(subTask.getId()).getDescription(), taskDesc,
                "Описание SubTask не совпадает до добавления и после добавления в manager ");
        assertEquals(taskManager.getSubTask(subTask.getId()).getStatus(), taskStatus,
                "Статус SubTask не совпадает до добавления и после добавления в manager ");
        assertEquals(taskManager.getSubTask(subTask.getId()).getEpicId(), epicId,
                "Epic у  SubTask не совпадает до добавления и после добавления в manager ");
    }

    @Test
    void isShouldBeIdIsNotNullAndExistInManagerWhenAddTask() {
        assertTrue(task1.getId() > 0, "Id должен быть >0");
        assertNotNull(taskManager.getTask(task1.getId()), "task1 не найден в taskManager");
    }

    @Test
    void isShouldBeIdIsNotNullAndExistInManagerWhenAddEpic() {
        assertTrue(epic1.getId() > 0, "Id должен быть >0");
        assertNotNull(taskManager.getEpic(epic1.getId()), "epic1 не найден в taskManager");
    }

    @Test
    void isShouldBeIdIsNotNullAndExistInManagerWhenAddSubtask() {
        assertTrue(subTask1.getId() > 0, "Id должен быть >0");
        assertNotNull(taskManager.getSubTask(subTask1.getId()), "subTask1 не найден в taskManager");
    }

    @Test
    void isShouldBeAddInEpicsSubTasksWhenAddSubTask() {
        assertEquals(epic1.getSubTaskIds().get(0), subTask1.getId(), "subtask1 не добавлен в список subtasks у Epic1");
    }

    @Test
    void isShouldBeUpdateTaskInManagerWhenUpdateTask() {
        task1.setName("Обновление задачи");
        task1.setDescription("Обновление задачи desc");
        taskManager.updateTask(task1);
        assertEquals(taskManager.getTask(task1.getId()).getName(), "Обновление задачи",
                "subtask1.name не обновился");
        assertEquals(taskManager.getTask(task1.getId()).getDescription(), "Обновление задачи desc",
                "subtask1.description не обновился");
    }

    @Test
    void isShouldBeUpdateEpicInManagerWhenUpdateEpic() {
        epic1.setName("Обновление эпика");
        epic1.setDescription("Обновление эпика desc");
        taskManager.updateEpic(epic1);
        assertEquals(taskManager.getEpic(epic1.getId()).getName(), "Обновление эпика",
                "epic1.name не обновился");
        assertEquals(taskManager.getEpic(epic1.getId()).getDescription(), "Обновление эпика desc",
                "epic1.description не обновился");
    }


    @Test
    void isShouldBeUpdateSubtaskInManagerWhenUpdateSubtask() {
        subTask1.setName("Обновление сабтаска");
        subTask1.setDescription("Обновление сабтаска desc");
        taskManager.updateSubTask(subTask1);
        assertEquals(taskManager.getSubTask(subTask1.getId()).getName(), "Обновление сабтаска",
                "subTask1.name не обновился");
        assertEquals(taskManager.getSubTask(subTask1.getId()).getDescription(), "Обновление сабтаска desc",
                "subTask1.description не обновился");
    }

    @Test
    void isShouldBeEpicStatusDoneWhenAllSubTasksDone() {
        subTask1.setStatus(TaskStatus.DONE);
        taskManager.updateSubTask(subTask1);
        subTask2.setStatus(TaskStatus.DONE);
        taskManager.updateSubTask(subTask2);
        assertEquals(epic1.getStatus(), TaskStatus.DONE, "epic1 не статусе DONE,а subtask.status=DONE");
    }

    @Test
    void isShouldBeEpicStatusNewWhenAllSubTasksNew() {
        subTask1.setStatus(TaskStatus.NEW);
        taskManager.updateSubTask(subTask1);
        subTask2.setStatus(TaskStatus.NEW);
        taskManager.updateSubTask(subTask2);
        assertEquals(epic1.getStatus(), TaskStatus.NEW, "epic1 не статусе DONE,а subtask.status=NEW");
    }

    @Test
    void isShouldBeEpicStatusInProgressWhenAllSubTaskNotNewAndNotDone() {
        subTask1.setStatus(TaskStatus.NEW);
        taskManager.updateSubTask(subTask1);
        subTask2.setStatus(TaskStatus.DONE);
        taskManager.updateSubTask(subTask2);
        assertEquals(epic1.getStatus(), TaskStatus.IN_PROGRESS, "epic1 не в статусе IN_PROGRESS," +
                " а subtasks.status in (NEW,DONE)");
    }

    @Test
    void isShouldBeTasksClearWhenTaksDeleteAll() {
        taskManager.deleteTasks();
        assertEquals(taskManager.getTasksList().size(), 0, "таски должны быть удалены");
    }

    @Test
    void isShouldBeEpicsAndSubtasksClearWhenEpicsDeleteAll() {
        taskManager.deleteEpics();
        assertEquals(taskManager.getSubTasksList().size(), 0, "сабтаски должны быть удалены");
        assertEquals(taskManager.getEpicsList().size(), 0, "епики должны быть удалены");
    }

    @Test
    void isShouldBeIdsInEpicAndSubtasksClearWhenSubtasksDeleteAll() {
        taskManager.deleteSubTasks();
        assertEquals(taskManager.getSubTasksList().size(), 0, "сабтаски должны быть удалены");
        assertTrue(epic1.getSubTaskIds().isEmpty(), "У epic1 не удален массив subtasks");
    }

    @Test
    void isShouldBeIdsInEpic1AndSubtask1DeleteWhenSubtasks1Delete() {
        taskManager.deleteSubTask(subTask1.getId());
        assertNotEquals(epic1.getSubTaskIds().get(0), subTask1.getId(), "У epic1 не удален subtask1");
        assertNotEquals(taskManager.getSubTasksList().get(0), subTask1.getId(), "subtask1 не удален");
    }

    @Test
    void isShouldBeEpic1DelAndEpicIdInSubtasksDelWhenEpic1Delete() {
        taskManager.deleteEpic(epic1.getId());
        assertNull(taskManager.getEpic(epic1.getId()), "epic1 не удален");
        assertTrue(epic1.getSubTaskIds().isEmpty(), "У epic1 не удален массив subtasks");
    }

    @Test
    void isShouldBeTask1InTaksClearWhenTaks1Del() {
        taskManager.deleteTask(task1.getId());
        assertEquals(taskManager.getTasksList().size(), 0, "task1 должен быть удален");
    }

    @Test
    void isShouldBeHistoryWhenGetTask1() {
        taskManager.getTask(task1.getId());
        assertEquals(taskManager.getHistory().size(), 1, "В истории более одной задачи");
        assertEquals(taskManager.getHistory().get(0), task1, "В истории не задача task1");
    }
}
