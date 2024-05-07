package test;

import manager.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ManagersTest {

    @Test
    void methodGetDefaultDontGetNull() { //Проверяем что менеджер возвращает TaskManager
        Assertions.assertNotNull(Managers.getDefault());
    }

    @Test
    void methodGetDefaultHistoryDontGetNull() { //Проверяем что менеджер возвращает HistoryManager
        Assertions.assertNotNull(Managers.getDefaultHistory());
    }
}