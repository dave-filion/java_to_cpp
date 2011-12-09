int main() {
#ifdef A
    for (; i; --i) {
	while (!(readb(base+T_STATUS_REG_OFFSET) & T_ST_RDY)) barrier();
#else
    while (!(readb(base+T_STATUS_REG_OFFSET) & T_ST_RDY)) barrier();
    for (; i; --i) {
#endif
	*d++ = readb(reg);
    }
}
