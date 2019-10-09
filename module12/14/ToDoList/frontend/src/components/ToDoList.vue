<template>
    <div class="to-do-list__container">
        <div class="to-do-list">
            <div class="d-table w-100 pl-4 pr-4">
                <div class="d-table-row">
                    <div class="d-table-cell align-top">
                        <h1 class="text-primary">ToDoList</h1>
                    </div>

                    <div class="d-table-cell align-top text-right">
                        <div class="to-do-list__task-count">
                            Всего задач: {{ taskCount }}
                        </div>

                        <div class="to-do-list__completed-task-count">
                            Из них завершенных: {{ completedTaskCount }}
                        </div>
                    </div>
                </div>
            </div>

            <div v-if="tasks !== null" class="to-do-list__new-task mt-2">
                <input
                    class="to-do-list__new-task-input"
                    type="text"
                    placeholder="+Новая задача (для добавления Enter)"
                    v-model="newTaskName"
                    @keyup.enter="createTask">
            </div>

            <div v-if="tasks !== null" class="mt-2">
                <slot
                    name="task"
                    v-for="task in tasks"
                    :task="task"
                    :remover="() => removeTask(task.dirty.id)"></slot>
            </div>

            <div v-else-if="loadError !== null" class="alert alert-danger mt-2 ml-4 mr-4">
                <p class="text-center">{{ loadError }}</p>

                <div class="text-center">
                    <button class="btn btn-sm btn-primary" type="button" @click="loadTasks">Повторить</button>
                </div>
            </div>

            <div v-else class="text-center mt-2 pt-5 pb-5">
                <div class="spinner-grow text-danger spinner-grow-sm"></div>
                <div class="spinner-grow text-success spinner-grow-sm"></div>
                <div class="spinner-grow text-primary spinner-grow-sm"></div>
            </div>
        </div>
    </div>
</template>

<style>
    .to-do-list__container {
        max-width: 600px;
        margin: 0 auto;
        padding: 1em 0;
    }

    .to-do-list__container {
        border-radius: .5em;
        padding: 1em 0;
        background-color: #fff;
    }

    .to-do-list__completed-task-count {
        font-size: .7em;
        color: #777;
    }

    .to-do-list__task-count {
        font-size: .8em;
        color: #555;
    }

    .to-do-list__new-task {
        border: 1px solid #ddd;
        border-left: none;
        border-right: none;
    }

    .to-do-list__new-task-input {
        width: 100%;
        padding: .75em 1em;
        border: none;
        outline: none;
    }
</style>

<script>
    import axios from 'axios';
    import ToDoTaskGuard from '../ToDoTaskGuard';

    export default {
        data() {
            return {
                tasks: null,
                loadError: null,
                newTaskName: '',
            };
        },

        computed: {
            taskCount() {
                return this.tasks !== null? this.tasks.length: 0;
            },

            completedTaskCount() {
                let count = 0;

                if(this.tasks !== null) {
                    for(let i = 0; i < this.tasks.length; i++) {
                        if(this.tasks[i].dirty.completed) {
                            count++;
                        }
                    }
                }

                return count;
            },
        },

        methods: {
            loadTasks() {
                this.tasks = null;
                this.loadError = null;

                axios.get("/tasks")
                     .then((response) => {
                         const tasks = [];

                         for(let i = 0; i < response.data.length; i++) {
                             tasks.push(new ToDoTaskGuard(response.data[i]));
                         }

                         this.tasks = tasks;
                     })
                     .catch((error) => {
                         window.console.log(error);
                         this.loadError = 'Не удалось загрузить список задач';
                     });
            },

            createTask() {
                const taskName = this.newTaskName.trim();

                if(taskName !== '') {
                    axios.post("/tasks", {name: taskName})
                         .then((response) => {
                             this.tasks.unshift(new ToDoTaskGuard(response.data));
                         })
                         .catch((error) => {
                             window.console.log(error);
                             this.$showError('Не удалось создать задачу, попробуйте снова')
                         });
                }

                this.newTaskName = '';
            },

            removeTask(id) {
                for(let i = 0; i < this.tasks.length; i++) {
                    if(this.tasks[i].dirty.id === id) {
                        this.tasks.splice(i, 1);
                        break;
                    }
                }
            },
        },

        mounted() {
            this.loadTasks();
        },
    };
</script>
