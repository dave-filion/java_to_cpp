/*{
}

{ 

#if A
hello; 
#else
#endif

}*/

extern f(), g();

int main() {

#ifdef A
{
#else
#endif

g();
f();

#ifdef A
}
#else
#endif

}

