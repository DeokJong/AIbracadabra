import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
export const vuetify = createVuetify({
  components,
  directives,
  theme: {
    defaultTheme: 'light',
  },
   defaults: {
    VDataTable: {
      ripple: false, // 모든 VDataTable 컴포넌트에서 ripple 끔
    },
  },
})
