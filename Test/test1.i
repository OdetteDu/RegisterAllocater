//This block uses each register twice and never used again. This will test if the code can free the physical register without spill.  
loadI 0 => r1
loadI 1 => r2
add r1, r2 => r3
add r2, r3 => r4
add r3, r4 => r5
add r4, r5 => r6
add r5, r6 => r7
add r6, r7 => r8
add r7, r8 => r9
add r8, r9 => r10
loadI 1024 => r11
loadI 1028 => r12
loadI 1032 => r13
loadI 1036 => r14
loadI 1040 => r15
load r11 => r15
store r15 => r11
store r10 => r12
output 1024
output 1028