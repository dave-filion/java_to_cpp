sayHi
{
   CallExpression(SelectionExpression(PrimaryIdentifier("System"), "out"), null, "println", Arguments(StringLiteral("\"Hi\"")))
;
}
sayHi_String
{
   CallExpression(SelectionExpression(PrimaryIdentifier("System"), "out"), null, "println", Arguments(StringLiteral("\"HIII\"")))
;
}
main_String
{
   CallExpression(SelectionExpression(PrimaryIdentifier("System"), "out"), null, "println", Arguments(StringLiteral("\"Hello world\"")))
;
}
toString
{
return "HELLO";
}
