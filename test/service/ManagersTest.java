package service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void isPositiveTaskManagerIsNotNull() {
        assertNotNull(Managers.getDefault(), "Объект TaskManager не проинициализрован");
    }
    @Test
    void isPositiveHistoryTaskManagerIsNotNull() {
        assertNotNull(Managers.getDefaultHistory(), "Объект HistoryManager не проинициализрован");
    }
}