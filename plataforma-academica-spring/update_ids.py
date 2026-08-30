import glob
import os

path = 'c:/Users/PAULO/OneDrive/Documentos/PlataformaAcademica/ProjetoFinal/plataforma-academica-spring/src/main/java/com/plataforma_academica/plataforma/model/*.java'
files = glob.glob(path)

for f in files:
    with open(f, 'r', encoding='utf-8') as file:
        content = file.read()
    
    modified = False
    if 'private Long id;' in content:
        content = content.replace('private Long id;', 'private UUID id;')
        modified = True
        
    if 'import java.util.UUID;' not in content and 'UUID' in content:
        content = content.replace('import jakarta.persistence.*;', 'import jakarta.persistence.*;\nimport java.util.UUID;')
        modified = True

    # Substituir @GeneratedValue(strategy = GenerationType.IDENTITY) em cima de id
    if '@GeneratedValue(strategy = GenerationType.IDENTITY)\n    private UUID id;' in content:
        content = content.replace(
            '@GeneratedValue(strategy = GenerationType.IDENTITY)\n    private UUID id;',
            '@Column(columnDefinition = "uuid", updatable = false, nullable = false)\n    private UUID id;'
        )
        modified = True

    if modified:
        with open(f, 'w', encoding='utf-8') as file:
            file.write(content)
        print(f"Atualizado: {os.path.basename(f)}")

print("Concluído!")
