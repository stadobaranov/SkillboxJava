<template>
    <div class="to-do-task">
        <template v-if="editing">
            <div>
                <input class="form-control w-100" type="text" v-model="editedName" @keyup.enter="stopEditing">
            </div>

            <div class="text-right mt-2">
                <button class="btn btn-sm btn-primary" @click="stopEditing">Применить</button>
                <button class="btn btn-sm btn-secondary ml-2" @click="cancelEditing">Отмена</button>
            </div>
        </template>
        <template v-else>
            <div class="custom-control custom-checkbox">
                <input
                    :id="comboBoxId"
                    class="custom-control-input"
                    type="checkbox"
                    v-model="record.dirty.completed"
                    @change="complete">

                <label
                    :for="comboBoxId"
                    class="custom-control-label"
                >{{ record.dirty.name }}</label>
            </div>

            <div class="mt-2">
                <span class="to-do-task__id">
                    #{{ record.dirty.id }}
                </span>

                <span class="to-do-task__datetime ml-2">
                    Создана: {{ record.dirty.createdAt }}
                </span>

                <span v-if="record.dirty.completedAt !== null" class="to-do-task__datetime ml-2">
                    Завершена: {{ record.dirty.completedAt }}
                </span>
            </div>

            <div class="mt-2 text-right">
                <button class="btn btn-sm btn-link" @click="refresh">Обновить</button>
                <button class="btn btn-sm btn-link ml-1" @click="startEditing">Редактировать</button>
                <button class="btn btn-sm btn-danger ml-1" @click="remove">Удалить</button>
            </div>
        </template>
    </div>
</template>

<style>
    .to-do-task {
        padding: .7em 1em;
        border-bottom: 1px solid #ddd;
    }

    .to-do-task:last-child {
        border-bottom: none;
    }

    .to-do-task__id,
    .to-do-task__datetime {
        font-size: .7em;
        color: #555;
    }
</style>

<script>
    import axios from 'axios';

    function ActionQueue(finisher) {
        const actions = [];

        const finishCurrentAction = () => {
            actions.shift();

            if(actions.length > 0) {
                actions[0](finishCurrentAction);
            }
            else {
                finisher();
            }
        };

        this.push = (action) => {
            actions.push(action);

            if(actions.length === 1) {
                action(finishCurrentAction);
            }
        };

        this.clear = () => {
            if(actions.length > 1) {
                actions.splice(1, actions.length - 1);
            }
        };
    }

    export default {
        props: {
            record: {
                type: Object,
                required: true,
            },

            remover: {
                type: Function,
                required: true,
            },
        },

        data() {
            return {
                editing: false,
                editedName: null,
            };
        },

        computed: {
            comboBoxId() {
                return 'to-do-task-' + this.record.dirty.id;
            },
        },

        methods: {
            refresh() {
                this.queue.push((finish) => {
                    axios.get("/tasks/" + this.record.dirty.id)
                        .then((response) => {
                            this.record.actual.copyOfRaw(response.data);
                            finish();
                        })
                        .catch((error) => {
                            window.console.log(error);

                            if(error.response.status !== 404) {
                                this.$showError("Не удалось перезагрузить задачу #" + this.record.dirty.id + ", попробуйте снова");
                            }
                            else {
                                this.$showError("Задача #" + this.record.dirty.id + " не существует");
                                this.remover();
                            }

                            finish();
                        });
                });
            },

            complete() {
                const value = this.record.dirty.completed;

                this.queue.push((finish) => {
                    axios.patch("/tasks/" + this.record.dirty.id + '/complete', {value: value})
                        .then((response) => {
                            this.record.actual.completed = value;
                            this.record.actual.completedAt = value? response.data: null;
                            finish();
                        })
                        .catch((error) => {
                            window.console.log(error);

                            if(error.response.status !== 404) {
                                this.$showError("Не удалось изменить состояние задачи #" + this.record.dirty.id + ", попробуйте снова");
                            }
                            else {
                                this.$showError("Задача #" + this.record.dirty.id + " не существует");
                                this.remover();
                            }

                            finish();
                        });
                });
            },

            startEditing() {
                this.editedName = this.record.dirty.name;
                this.editing = true;
            },

            cancelEditing() {
                this.editing = false;
            },

            stopEditing() {
                const name = this.editedName;

                this.queue.push((finish) => {
                    axios.patch("/tasks/" + this.record.dirty.id + '/rename', {name: name})
                        .then(() => {
                            this.record.actual.name = name;
                            finish();
                        })
                        .catch((error) => {
                            window.console.log(error);

                            if(error.response.status !== 404) {
                                this.$showError("Не удалось переименовать задачу #" + this.record.dirty.id + ", попробуйте снова");
                            }
                            else {
                                this.$showError("Задача #" + this.record.dirty.id + " не существует");
                                this.remover();
                            }

                            finish();
                        });
                });

                this.editing = false;
            },

            remove() {
                axios.delete("/tasks/" + this.record.dirty.id)
                    .then(() => {
                        this.remover();
                    })
                    .catch((error) => {
                        window.console.log(error);

                        if(error.response.status !== 404) {
                            this.$showError("Не удалось удалить задачу #" + this.record.dirty.id + ", попробуйте снова")
                        }
                        else {
                            this.remover();
                        }
                    });
            },
        },

        created() {
            this.queue = new ActionQueue(() => this.record.rollback());
        },

        destroyed() {
            this.queue.clear();
            this.queue = null;
        },
    };
</script>
