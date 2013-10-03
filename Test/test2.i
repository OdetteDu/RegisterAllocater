//This block is playing with live range. r1 has the longest live range, r1 has least.
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
loadI 1036 => r14
loadI 1032 => r13
loadI 1028 => r12
loadI 1024 => r11

add r8, r9 => r10
add r7, r8 => r9
add r6, r7 => r8
add r5, r6 => r7
add r4, r5 => r6
add r3, r4 => r5
add r2, r3 => r4
add r1, r2 => r3

store r3 => r11
output 1024