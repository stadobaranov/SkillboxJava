function ToDoTask(rawData) {
    this.copyOfRaw(rawData);
}

ToDoTask.prototype.copyOfRaw = function(rawData) {
    this.id = rawData.id;
    this.name = rawData.name;
    this.createdAt = rawData.createdAt;
    this.completedAt = rawData.completedAt;
    this.completed = rawData.completedAt !== null;
};

ToDoTask.prototype.copyOf = function(other) {
    this.id = other.id;
    this.name = other.name;
    this.createdAt = other.createdAt;
    this.completedAt = other.completedAt;
    this.completed = other.completed;
};

export default ToDoTask;