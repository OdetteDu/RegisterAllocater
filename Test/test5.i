//This test will create register with same frequency, but one already spilled, the other not.
loadI 4 => r1
loadI 8 => r2
loadI 1028 => r7
load r7 => r50
add r1, r2=>r3
sub r1, r2=>r4
mult r1, r2=>r5
lshift r1, r2=>r6
loadI 1024 => r8
load r8 => r51
add r50, r51 => r52
add r1, r2=>r9
sub r1, r2=>r10
mult r1, r2=>r11
lshift r1, r2=>r12
loadI 1032 => r13
store r52 => r13
output 1032

//This test will create register with same frequency, but one is constant, the other not.
loadI 4 => r1
loadI 8 => r2
loadI 1028 => r7
load r7 => r50
add r1, r2=>r3
sub r1, r2=>r4
mult r1, r2=>r5
lshift r1, r2=>r6
loadI 100 => r51
add r50, r51 => r52
add r1, r2=>r9
sub r1, r2=>r10
mult r1, r2=>r11
lshift r1, r2=>r12
loadI 1032 => r13
store r52 =>r13 
output 1032