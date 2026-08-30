import glob
import os

path = 'c:/Users/PAULO/OneDrive/Documentos/PlataformaAcademica/ProjetoFinal/plataforma-academica-spring/src/main/java/com/plataforma_academica/plataforma/dto/*.java'
files = glob.glob(path)

for f in files:
    with open(f, 'r', encoding='utf-8') as file:
        content = file.read()
    
    modified = False
    if 'private Long id;' in content:
        content = content.replace('private Long id;', 'private UUID id;')
        modified = True
        
    if 'private UUID id;' in content and 'import java.util.UUID;' not in content:
        if 'package com.plataforma_academica.plataforma.dto;' in content:
            content = content.replace('package com.plataforma_academica.plataforma.dto;', 'package com.plataforma_academica.plataforma.dto;\n\nimport java.util.UUID;')
            modified = True

    if modified:
        with open(f, 'w', encoding='utf-8') as file:
            file.write(content)
        print(f"DTO atualizado para UUID: {os.path.basename(f)}")

print("Migração de DTOs concluída!")
