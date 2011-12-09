static void hotkey_compare_and_issue_event(struct tp_nvram_state *oldn,
        struct tp_nvram_state *newn,
        const u32 event_mask)
{
# 2365 "drivers/platform/x86/thinkpad_acpi.c"
 void issue_volchange(const unsigned int oldvol,
        const unsigned int newvol)
 {
  unsigned int i = oldvol;

  while (i > newvol) {
   do { if (event_mask & (1 << TP_ACPI_HOTKEYSCAN_VOLUMEDOWN)) tpacpi_hotkey_send_key(TP_ACPI_HOTKEYSCAN_VOLUMEDOWN); } while (0);
   i--;
  }
  while (i < newvol) {
   do { if (event_mask & (1 << TP_ACPI_HOTKEYSCAN_VOLUMEUP)) tpacpi_hotkey_send_key(TP_ACPI_HOTKEYSCAN_VOLUMEUP); } while (0);
   i++;
  }
 }

 void issue_brightnesschange(const unsigned int oldbrt,
        const unsigned int newbrt)
 {
  unsigned int i = oldbrt;

  while (i > newbrt) {
   do { if (event_mask & (1 << TP_ACPI_HOTKEYSCAN_FNEND)) tpacpi_hotkey_send_key(TP_ACPI_HOTKEYSCAN_FNEND); } while (0);
   i--;
  }
  while (i < newbrt) {
   do { if (event_mask & (1 << TP_ACPI_HOTKEYSCAN_FNHOME)) tpacpi_hotkey_send_key(TP_ACPI_HOTKEYSCAN_FNHOME); } while (0);
   i++;
  }
 }
}
