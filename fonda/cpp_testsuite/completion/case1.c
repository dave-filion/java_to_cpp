int main() {
for (
#if BY_ROW
i = 0; i < R; i++)
s += a[i];
#elif BY_COL
j = 0; j < C; j++)
s += a[j];
#endif
}
