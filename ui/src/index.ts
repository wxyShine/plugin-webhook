import { definePlugin } from "@halo-dev/console-shared";
import HomeView from "./views/HomeView.vue";
import { markRaw } from "vue";
// https://icon-sets.iconify.design/logos/webhooks/
import LogosWebhooks from '~icons/logos/webhooks';

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
            icon: markRaw(LogosWebhooks),
            priority: 0,
          },
        },
      },
    },
  ],
  extensionPoints: {},
});
