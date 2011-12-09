int main() {



#ifdef CONFIG_A

  while (a) {




  }

#elif defined CONFIG_B

#ifdef CONFIG_C
  while (outer) {
#endif


  while (b) {




  }

#ifdef CONFIG_C
  }
#endif


#endif










}
