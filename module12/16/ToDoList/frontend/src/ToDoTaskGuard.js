import ToDoTask from './ToDoTask';

function ToDoTaskGuard(rawData) {
    this.actual = new ToDoTask(rawData);
    this.dirty = new ToDoTask(rawData);
}

ToDoTaskGuard.prototype.rollback = function() {
    this.dirty.copyOf(this.actual);
};

export default ToDoTaskGuard;