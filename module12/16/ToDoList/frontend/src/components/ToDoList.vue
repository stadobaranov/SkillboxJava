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

            <div class="to-do-list__new-task mt-2">
                <input
                    class="to-do-list__new-task-input"
                    type="text"
                    placeholder="+Новая задача (для добавления Enter)"
                    v-model="newTaskName"
                    @keyup.enter="createTask">
            </div>

            <div class="mt-2">
                <slot
                    name="task"
                    v-for="task in tasks"
                    :task="task"
                    :remover="() => removeTask(task.dirty.id)"></slot>
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
            const guards = [];

            for(let i = 0; i < window.tasks.length; i++) {
                guards.push(new ToDoTaskGuard(window.tasks[i]));
            }

            return {
                tasks: guards,
                newTaskName: '',
            };
        },

        computed: {
            taskCount() {
                return this.tasks.length;
            },

            completedTaskCount() {
                let count = 0;

                for(let i = 0; i < this.tasks.length; i++) {
                    if(this.tasks[i].dirty.completed) {
                        count++;
                    }
                }

                return count;
            },
        },

        methods: {
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
    };
</script>
