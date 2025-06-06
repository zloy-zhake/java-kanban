class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @Override
    InMemoryTaskManager createTaskManager() {
        Managers taskManagerUtil = new Managers();
        return (InMemoryTaskManager) taskManagerUtil.getDefault();
    }
}