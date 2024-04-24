import { definePlugin } from "@halo-dev/console-shared";
import HomeView from "./views/HomeView.vue";
import { IconPlug } from "@halo-dev/components";
import { markRaw } from "vue";

export default definePlugin({
  components: {},
  routes: [
    {
        parentName: "ToolsRoot",      route: {
        path: "/webhook",
        name: "Webhook",
        component: HomeView,
        meta: {
          title: "Webhook管理",
          searchable: true,
          menu: {
            name: "Webhook",
            group: "tool",
            icon: markRaw(IconPlug),
            priority: 0,
          },
        },
      },
    },
  ],
  extensionPoints: {},
});
