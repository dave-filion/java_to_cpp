#define FOO ## 1
#define BAR 1 ##
#define MACRO 1 ## ## 2
MACRO
#define NOTVALID ID ## ;
NOTVALID
#define COMMENT / ## *
COMMENT
#define name(x) ID_ ## x
name(; 2 3)
