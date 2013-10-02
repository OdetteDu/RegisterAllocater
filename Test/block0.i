loadI 1028=> r1
load r1 =>r2
mult r1,r2 => r3
loadI 1032 => r4
sub r4,r2 => r5
loadI 1036 => r6
mult r5,r6 => r7
sub r7,r3 => r8
store r8 => r1
output 1028