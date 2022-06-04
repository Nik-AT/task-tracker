package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryTaskManagerTest extends ManagerTest<InMemoryTaskManager> {
    @BeforeEach
    void initInMemoryTaskManager() {
        inMemoryTaskManager = new InMemoryTaskManager();
        init();
    }

    @Test
    void IInMemoryTaskManagerTest() {
        inMemoryTaskManager = new InMemoryTaskManager();
        assertEquals(0, inMemoryTaskManager.taskHashMap.size(), "Задач нет");
        assertEquals(0, inMemoryTaskManager.subTaskHashMap.size(), "Задач нет");
        assertEquals(0, inMemoryTaskManager.epicHashMap.size(), "Задач нет");
        assertEquals(0, inMemoryTaskManager.historyManager.getHistory().size(), "Задач нет");
    }
}