package service;

import org.junit.jupiter.api.BeforeEach;

class InMemoryTaskManagerTest extends AbstractTaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    @Override
    void beforeEach() {
        taskManager = new InMemoryTaskManager();
        super.beforeEach();
    }
}
