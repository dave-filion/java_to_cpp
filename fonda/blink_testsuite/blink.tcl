#!/usr/bin/expect

global blink_prompt
set blink_prompt "\[(\]bdb-.+\[)\] "

proc blink_begin {prog} {
    global blink_prompt
    global spawn_id
    global env
    set timeout 10
    spawn java -ea xtc.lang.blink.Blink ${prog}
    expect {
        -re "Blink a .+\n${blink_prompt}" {return}
        timeout {
            puts "timeout"
            exit -1
        }
    }
}

proc blink_begin_opt {prog opt} {
    global blink_prompt
    global spawn_id
    global env
    set timeout 10
    spawn java -ea xtc.lang.blink.Blink ${opt} ${prog}
    expect {
        -re "Blink a .+\n${blink_prompt}" {return}
        timeout {
            puts "timeout"
            exit -1
        }
    }
}

proc blink_cmd {cmd p} {
    global blink_prompt
    global spawn_id
    set timeout 5
    send "${cmd}\n"
    expect {
        -re "${p}${blink_prompt}" {return}
        eof {
            puts "The program exited"
        }
        timeout {
            puts "dumping the log message..."
            send "bdb log\n"
            expect {
                -re "${blink_prompt}" {
                    puts "timeout"
                    exit -1
                }
                eof {
                    puts "The program exited"
                }
                timeout {
                    puts "timeout"
                    exit -1
                }
            }
        }
    }
}

proc blink_end {cmd} {
    global blink_prompt
    global spawn_id
    set timeout 20
    send "${cmd}\n"
    expect {
        eof {return}
        timeout {
            puts "dumping the log message..."
            send "bdb log\n"
            expect {
                -re "${blink_prompt}" {
                    puts "timeout"
                    exit -1
                }
                timeout {
                    puts "timeout"
                    exit -1
                }
            }
        }
    }
}

