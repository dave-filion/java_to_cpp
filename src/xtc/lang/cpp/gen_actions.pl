#!/usr/bin/perl -w

# xtc - The eXTensible Compiler
# Copyright (C) 2009-2011 New York University
#
# This program is free software; you can redistribute it and/or
# modify it under the terms of the GNU General Public License
# version 2 as published by the Free Software Foundation.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301,
# USA.

#this file extracts node annotations of the form
#nonterminal.name:  /* nodetype */
#from a bison grammar
#the output can be passed to NodeTypeGenerator to create isNodeType functions
#and the semantic action java interface as NodeType.java

my %valid_annotations
    = map { $_ => 1 } ( "list", "layout", "action", "passthrough", "complete");


while(<STDIN>) {
    my($line) = $_;
    my($text) = $line;
    if ($text =~ m/^(.*)\/\*\*([^\*]+)\*\*\//) {
        $symbol = $1;
        $annotations = $2;

        
        if ($symbol =~ m/^([\w\d\._]+):/) {
            $symbol = $1;
        } elsif ($symbol =~ m/^%token ([\w\d\._]+)/) {
            $symbol = $1;
        } else {
            $symbol = "";
        }

        if (length($symbol) > 0) {
            @a = split(/,/, $annotations);
            for my $annotation (@a) {
                $annotation =~ s/^\s*(.*?)\s*$/$1/;
                if (exists($valid_annotations{$annotation})) {
                    print "$symbol $annotation\n"
                } else {
                    print STDERR
                        "warning: invalid annotation name $annotation\n";
                }
            }
        }
    }
}

#cat c.y | grep '^[A-Za-z0-9._]*: */\*\* *list *\*\*/' | awk -F: '{print $1, "list"}'
#cat c.y | grep '^[A-Za-z0-9._]*: */\*\* *action *\*\*/' | awk -F: '{print $1, "action"}'
#cat c.y | grep '^%token *[A-Za-z0-9._]* */\*\* *layout *\*\*/' | awk '{print $2, "layout"}'
#cat c.y | grep '^[A-Za-z0-9._]*: */\*\* *passthrough *\*\*/' | awk -F: '{print $1, "passthrough"}'

