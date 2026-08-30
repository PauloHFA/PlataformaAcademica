# Script para corrigir todos os valores Long para UUID nos testes
$files = Get-ChildItem "src/test/java/com/plataforma_academica/plataforma/**/*.java"
$uuid1 = "00000000-0000-0000-0000-000000000001"
$uuid2 = "00000000-0000-0000-0000-000000000002"
$uuid3 = "00000000-0000-0000-0000-000000000003"
$uuid4 = "00000000-0000-0000-0000-000000000004"
$uuid5 = "00000000-0000-0000-0000-000000000005"

foreach ($file in $files) {
    $c = [System.IO.File]::ReadAllText($file.FullName, [System.Text.Encoding]::UTF8)
    $original = $c
    
    # Adicionar import UUID se não existir
    if ($c -notmatch 'import java\.util\.UUID;') {
        $c = $c -replace '(package [^;]+;)', "`$1`nimport java.util.UUID;"
    }
    
    # Substituir IDs específicos
    $c = $c -replace '\b1L\b', "UUID.fromString(`"$uuid1`")"
    $c = $c -replace '\b2L\b', "UUID.fromString(`"$uuid2`")"
    $c = $c -replace '\b3L\b', "UUID.fromString(`"$uuid3`")"
    $c = $c -replace '\b4L\b', "UUID.fromString(`"$uuid4`")"
    $c = $c -replace '\b5L\b', "UUID.fromString(`"$uuid5`")"
    
    # Substituir em mocks do service - replaceAny(1L, ...) para replaceAny(uuid1, ...)
    $c = $c -replace 'replaceAny\(1L,', 'replaceAny(UUID.fromString("00000000-0000-0000-0000-000000000001"),'
    $c = $c -replace 'replaceAny\(2L,', 'replaceAny(UUID.fromString("00000000-0000-0000-0000-000000000002"),'
    $c = $c -replace 'replaceAny\(3L,', 'replaceAny(UUID.fromString("00000000-0000-0000-0000-000000000003"),'
    
    # Substituir todos os valores long por UUID.randomUUID()
    $c = $c -replace '\b\d+L\b', "UUID.randomUUID()"
    
    if ($c -ne $original) {
        [System.IO.File]::WriteAllText($file.FullName, $c, (New-Object System.Text.UTF8Encoding $false))
        Write-Output "Atualizado: $($file.Name)"
    }
}
Write-Output "Concluído!"
