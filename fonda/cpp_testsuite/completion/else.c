void
prom_printf(char *fmt, ...)
{
	va_list args;
	char ch, *bptr;
	int i;

	va_start(args, fmt);

#ifdef CONFIG_KGDB
	ppbuf[0] = 'O';
	i = vsprintf(ppbuf + 1, fmt, args) + 1;
#else
	i = vsprintf(ppbuf, fmt, args);
#endif

	bptr = ppbuf;

#ifdef CONFIG_KGDB
	if (kgdb_initialized) {
		printk("kgdb_initialized = %d\n", kgdb_initialized);
		putpacket(bptr, 1);
	} else
#else
	while((ch = *(bptr++)) != 0) {
		if(ch == '\n')
			prom_putchar('\r');

		prom_putchar(ch);
	}
#endif
	va_end(args);
	return;
}

