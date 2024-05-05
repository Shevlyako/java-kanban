package Test;

import Manager.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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