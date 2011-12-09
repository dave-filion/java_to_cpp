/* comment */
#define MISC_MAJOR 1
#define MOUSEDEV_MIX 1
#define MOUSEDEV_MINOR_BASE 1
extern int imajor(int), iminor(int);

int main() {
  int i;

#ifdef CONFIG_INPUT_MOUSEDEV_PSAUX
  if (imajor(inode) == MISC_MAJOR)
   i = MOUSEDEV_MIX;
  else
#endif
   i = iminor(inode) - MOUSEDEV_MINOR_BASE;
}
