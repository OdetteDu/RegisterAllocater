//This class will make both top down and bottom up very different.
loadI 1024 => r1 
loadI 1028 => r2 
loadI 1032 => r3
loadI 1036 => r4 
loadI 1040 => r5 
loadI 4 => r6
add r1, r6 => r7
sub r2, r6 => r8
mult r3, r6 => r9
lshift r4, r6 => r10
rshift r5, r6 => r11
add r5, r6 => r12
sub r5, r6 => r13
mult r5, r6 => r14
lshift r5, r6 => r15
rshift r4, r6 => r16
add r4, r6 => r17
sub r4, r6 => r18
mult r4, r6 => r19
lshift r3, r6 => r20
rshift r3, r6 => r21
add r3, r6 => r22
sub r2, r6 => r23
mult r2, r6 => r24
add r1, r6 => r25
store r10 => r1
store r14 => r2
store r18 => r3
store r21 => r4
store r25 => r5
output 1024
output 1028
output 1032
output 1036
output 1040