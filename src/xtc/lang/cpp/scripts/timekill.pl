#!/usr/bin/perl -w

# Run my_program w/ the given arguments.  If my_program is still running 
# after the specified number of seconds, kill it, and tell the user.

my $timeout = $ARGV[0];
shift @ARGV;

my $progname = $ARGV[0];
shift @ARGV;

eval {
  local $SIG{ALRM} = sub { die "alarm\n" };
  alarm $timeout;
  foreach $argnum (0 .. $#ARGV) {
    $command .= " $ARGV[$argnum]";
  }
  print qx($command);
  alarm 0;
};

if($@){
  die unless $@ eq "alarm\n";
  print "TIMEOUT $command\n";
  qx(ps -ef | grep "$progname" | egrep -v " grep | vi | more | cat | pg | egrep " | awk '{print \$2}' | xargs kill );
}
