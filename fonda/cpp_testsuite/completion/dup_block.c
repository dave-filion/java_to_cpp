//to test what happens when a block state, used to support the essential
//forkAsNeeded optimization, is duplication to assure conditional directive
//boundaries for lists of complete syntactic units

#if A

#if B
a
#else
b
#endif

;


#else


#endif
