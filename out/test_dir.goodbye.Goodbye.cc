sayHi
{
   CallExpression(SelectionExpression(PrimaryIdentifier("System"), "out"), null, "println", Arguments(StringLiteral("\"Bye!\"")))
;
}
sayBye_String
{
   CallExpression(SelectionExpression(PrimaryIdentifier("System"), "out"), null, "println", Arguments(PrimaryIdentifier("name")))
;
}
sayBye
{
   CallExpression(SelectionExpression(PrimaryIdentifier("System"), "out"), null, "println", Arguments(StringLiteral("\"BYE\"")))
;
}
forTest
{
   int q = 5;
   for(int i = 0;i<10;i++){
      q+=1;
   }
}
whileTest
{
   int i = 0;
   while (i<10){
      i++;
   }
}
returnString
{
return "Hi";
}
chainTest
{
   CallExpression(CallExpression(null, null, "returnString", Arguments()), null, "startsWith", Arguments(StringLiteral("\"H\"")))
;
}
