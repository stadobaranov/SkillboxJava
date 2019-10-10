(function(t){function e(e){for(var n,s,a=e[0],c=e[1],d=e[2],u=0,p=[];u<a.length;u++)s=a[u],Object.prototype.hasOwnProperty.call(o,s)&&o[s]&&p.push(o[s][0]),o[s]=0;for(n in c)Object.prototype.hasOwnProperty.call(c,n)&&(t[n]=c[n]);l&&l(e);while(p.length)p.shift()();return i.push.apply(i,d||[]),r()}function r(){for(var t,e=0;e<i.length;e++){for(var r=i[e],n=!0,a=1;a<r.length;a++){var c=r[a];0!==o[c]&&(n=!1)}n&&(i.splice(e--,1),t=s(s.s=r[0]))}return t}var n={},o={app:0},i=[];function s(e){if(n[e])return n[e].exports;var r=n[e]={i:e,l:!1,exports:{}};return t[e].call(r.exports,r,r.exports,s),r.l=!0,r.exports}s.m=t,s.c=n,s.d=function(t,e,r){s.o(t,e)||Object.defineProperty(t,e,{enumerable:!0,get:r})},s.r=function(t){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(t,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(t,"__esModule",{value:!0})},s.t=function(t,e){if(1&e&&(t=s(t)),8&e)return t;if(4&e&&"object"===typeof t&&t&&t.__esModule)return t;var r=Object.create(null);if(s.r(r),Object.defineProperty(r,"default",{enumerable:!0,value:t}),2&e&&"string"!=typeof t)for(var n in t)s.d(r,n,function(e){return t[e]}.bind(null,n));return r},s.n=function(t){var e=t&&t.__esModule?function(){return t["default"]}:function(){return t};return s.d(e,"a",e),e},s.o=function(t,e){return Object.prototype.hasOwnProperty.call(t,e)},s.p="/";var a=window["webpackJsonp"]=window["webpackJsonp"]||[],c=a.push.bind(a);a.push=e,a=a.slice();for(var d=0;d<a.length;d++)e(a[d]);var l=c;i.push([0,"chunk-vendors"]),r()})({0:function(t,e,r){t.exports=r("56d7")},4154:function(t,e,r){"use strict";var n=r("f4ac"),o=r.n(n);o.a},4482:function(t,e,r){},"56d7":function(t,e,r){"use strict";r.r(e);r("cadf"),r("551c"),r("f751"),r("097d"),r("f9e3"),r("db43");var n=r("2b0e"),o=function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("div",{staticClass:"to-do-application"},[r("to-do-list",{scopedSlots:t._u([{key:"task",fn:function(t){return r("to-do-task",{key:t.task.dirty.id,attrs:{record:t.task,remover:t.remover}})}}])}),r("div",{staticClass:"to-do-application__errors"},t._l(t.errors,(function(e,n){return r("div",{key:e,staticClass:"alert alert-danger"},[r("h4",{staticClass:"alert-heading"},[t._v("Упс, что-то пошло не так")]),r("p",[t._v(t._s(e))]),r("hr"),r("button",{staticClass:"btn btn-block btn-outline-danger",attrs:{type:"button"},on:{click:function(e){return t.errors.splice(n,1)}}},[t._v("Закрыть")])])})),0)],1)},i=[],s=function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("div",{staticClass:"to-do-list__container"},[r("div",{staticClass:"to-do-list"},[r("div",{staticClass:"d-table w-100 pl-4 pr-4"},[r("div",{staticClass:"d-table-row"},[t._m(0),r("div",{staticClass:"d-table-cell align-top text-right"},[r("div",{staticClass:"to-do-list__task-count"},[t._v("\n                        Всего задач: "+t._s(t.taskCount)+"\n                    ")]),r("div",{staticClass:"to-do-list__completed-task-count"},[t._v("\n                        Из них завершенных: "+t._s(t.completedTaskCount)+"\n                    ")])])])]),r("div",{staticClass:"to-do-list__new-task mt-2"},[r("input",{directives:[{name:"model",rawName:"v-model",value:t.newTaskName,expression:"newTaskName"}],staticClass:"to-do-list__new-task-input",attrs:{type:"text",placeholder:"+Новая задача (для добавления Enter)"},domProps:{value:t.newTaskName},on:{keyup:function(e){return!e.type.indexOf("key")&&t._k(e.keyCode,"enter",13,e.key,"Enter")?null:t.createTask(e)},input:function(e){e.target.composing||(t.newTaskName=e.target.value)}}})]),r("div",{staticClass:"mt-2"},[t._l(t.tasks,(function(e){return t._t("task",null,{task:e,remover:function(){return t.removeTask(e.dirty.id)}})}))],2)])])},a=[function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("div",{staticClass:"d-table-cell align-top"},[r("h1",{staticClass:"text-primary"},[t._v("ToDoList")])])}],c=r("bc3a"),d=r.n(c);r("7f7f");function l(t){this.copyOfRaw(t)}l.prototype.copyOfRaw=function(t){this.id=t.id,this.name=t.name,this.createdAt=t.createdAt,this.completedAt=t.completedAt,this.completed=null!==t.completedAt},l.prototype.copyOf=function(t){this.id=t.id,this.name=t.name,this.createdAt=t.createdAt,this.completedAt=t.completedAt,this.completed=t.completed};var u=l;function p(t){this.actual=new u(t),this.dirty=new u(t)}p.prototype.rollback=function(){this.dirty.copyOf(this.actual)};var f=p,m={data:function(){for(var t=[],e=0;e<window.tasks.length;e++)t.push(new f(window.tasks[e]));return{tasks:t,newTaskName:""}},computed:{taskCount:function(){return this.tasks.length},completedTaskCount:function(){for(var t=0,e=0;e<this.tasks.length;e++)this.tasks[e].dirty.completed&&t++;return t}},methods:{createTask:function(){var t=this,e=this.newTaskName.trim();""!==e&&d.a.post("/tasks",{name:e}).then((function(e){t.tasks.unshift(new f(e.data))})).catch((function(e){window.console.log(e),t.$showError("Не удалось создать задачу, попробуйте снова")})),this.newTaskName=""},removeTask:function(t){for(var e=0;e<this.tasks.length;e++)if(this.tasks[e].dirty.id===t){this.tasks.splice(e,1);break}}}},h=m,v=(r("cf4e"),r("2877")),y=Object(v["a"])(h,s,a,!1,null,null,null),k=y.exports,b=function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("div",{staticClass:"to-do-task"},[t.editing?[r("div",[r("input",{directives:[{name:"model",rawName:"v-model",value:t.editedName,expression:"editedName"}],staticClass:"form-control w-100",attrs:{type:"text"},domProps:{value:t.editedName},on:{keyup:function(e){return!e.type.indexOf("key")&&t._k(e.keyCode,"enter",13,e.key,"Enter")?null:t.stopEditing(e)},input:function(e){e.target.composing||(t.editedName=e.target.value)}}})]),r("div",{staticClass:"text-right mt-2"},[r("button",{staticClass:"btn btn-sm btn-primary",on:{click:t.stopEditing}},[t._v("Применить")]),r("button",{staticClass:"btn btn-sm btn-secondary ml-2",on:{click:t.cancelEditing}},[t._v("Отмена")])])]:[r("div",{staticClass:"custom-control custom-checkbox"},[r("input",{directives:[{name:"model",rawName:"v-model",value:t.record.dirty.completed,expression:"record.dirty.completed"}],staticClass:"custom-control-input",attrs:{id:t.comboBoxId,type:"checkbox"},domProps:{checked:Array.isArray(t.record.dirty.completed)?t._i(t.record.dirty.completed,null)>-1:t.record.dirty.completed},on:{change:[function(e){var r=t.record.dirty.completed,n=e.target,o=!!n.checked;if(Array.isArray(r)){var i=null,s=t._i(r,i);n.checked?s<0&&t.$set(t.record.dirty,"completed",r.concat([i])):s>-1&&t.$set(t.record.dirty,"completed",r.slice(0,s).concat(r.slice(s+1)))}else t.$set(t.record.dirty,"completed",o)},t.complete]}}),r("label",{staticClass:"custom-control-label",attrs:{for:t.comboBoxId}},[t._v(t._s(t.record.dirty.name))])]),r("div",{staticClass:"mt-2"},[r("span",{staticClass:"to-do-task__id"},[t._v("\n                #"+t._s(t.record.dirty.id)+"\n            ")]),r("span",{staticClass:"to-do-task__datetime ml-2"},[t._v("\n                Создана: "+t._s(t.record.dirty.createdAt)+"\n            ")]),null!==t.record.dirty.completedAt?r("span",{staticClass:"to-do-task__datetime ml-2"},[t._v("\n                Завершена: "+t._s(t.record.dirty.completedAt)+"\n            ")]):t._e()]),r("div",{staticClass:"mt-2 text-right"},[r("button",{staticClass:"btn btn-sm btn-link",on:{click:t.refresh}},[t._v("Обновить")]),r("button",{staticClass:"btn btn-sm btn-link ml-1",on:{click:t.startEditing}},[t._v("Редактировать")]),r("button",{staticClass:"btn btn-sm btn-danger ml-1",on:{click:t.remove}},[t._v("Удалить")])])]],2)},_=[];function w(t){var e=[],r=function r(){e.shift(),e.length>0?e[0](r):t()};this.push=function(t){e.push(t),1===e.length&&t(r)},this.clear=function(){e.length>1&&e.splice(1,e.length-1)}}var g={props:{record:{type:Object,required:!0},remover:{type:Function,required:!0}},data:function(){return{editing:!1,editedName:null}},computed:{comboBoxId:function(){return"to-do-task-"+this.record.dirty.id}},methods:{refresh:function(){var t=this;this.queue.push((function(e){d.a.get("/tasks/"+t.record.dirty.id).then((function(r){t.record.actual.copyOfRaw(r.data),e()})).catch((function(r){window.console.log(r),404!==r.response.status?t.$showError("Не удалось перезагрузить задачу #"+t.record.dirty.id+", попробуйте снова"):(t.$showError("Задача #"+t.record.dirty.id+" не существует"),t.remover()),e()}))}))},complete:function(){var t=this,e=this.record.dirty.completed;this.queue.push((function(r){d.a.patch("/tasks/"+t.record.dirty.id+"/complete",{value:e}).then((function(n){t.record.actual.completed=e,t.record.actual.completedAt=e?n.data:null,r()})).catch((function(e){window.console.log(e),404!==e.response.status?t.$showError("Не удалось изменить состояние задачи #"+t.record.dirty.id+", попробуйте снова"):(t.$showError("Задача #"+t.record.dirty.id+" не существует"),t.remover()),r()}))}))},startEditing:function(){this.editedName=this.record.dirty.name,this.editing=!0},cancelEditing:function(){this.editing=!1},stopEditing:function(){var t=this,e=this.editedName;this.queue.push((function(r){d.a.patch("/tasks/"+t.record.dirty.id+"/rename",{name:e}).then((function(){t.record.actual.name=e,r()})).catch((function(e){window.console.log(e),404!==e.response.status?t.$showError("Не удалось переименовать задачу #"+t.record.dirty.id+", попробуйте снова"):(t.$showError("Задача #"+t.record.dirty.id+" не существует"),t.remover()),r()}))})),this.editing=!1},remove:function(){var t=this;d.a.delete("/tasks/"+this.record.dirty.id).then((function(){t.remover()})).catch((function(e){window.console.log(e),404!==e.response.status?t.$showError("Не удалось удалить задачу #"+t.record.dirty.id+", попробуйте снова"):t.remover()}))}},created:function(){var t=this;this.queue=new w((function(){return t.record.rollback()}))},destroyed:function(){this.queue.clear(),this.queue=null}},C=g,x=(r("4154"),Object(v["a"])(C,b,_,!1,null,null,null)),E=x.exports,O={components:{"to-do-list":k,"to-do-task":E},computed:{errors:function(){return this.$root.errors}}},$=O,A=(r("699c"),Object(v["a"])($,o,i,!1,null,null,null)),N=A.exports;n["a"].config.productionTip=!1,n["a"].prototype.$showError=function(t){this.$root.errors.unshift(t)},new n["a"]({data:{errors:[]},el:"#to-do-application",render:function(t){return t(N)}})},"699c":function(t,e,r){"use strict";var n=r("a871"),o=r.n(n);o.a},a871:function(t,e,r){},cf4e:function(t,e,r){"use strict";var n=r("4482"),o=r.n(n);o.a},db43:function(t,e,r){},f4ac:function(t,e,r){}});
//# sourceMappingURL=app.e29049fa.js.map