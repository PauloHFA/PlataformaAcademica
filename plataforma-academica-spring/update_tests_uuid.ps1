$files = Get-ChildItem "src/test/java/com/plataforma_academica/plataforma/**/*.java"
foreach ($file in $files) {
    $c = [System.IO.File]::ReadAllText($file.FullName, [System.Text.Encoding]::UTF8)
    # Substituir valores long/Long por UUID.randomUUID() ou UUID.fromString()
    # Primeiro, adicionar import UUID se não existir
    if ($c -notmatch 'import java\.util\.UUID;') {
        $c = $c -replace '(package [^;]+;)', "`$1`nimport java.util.UUID;"
    }
    # Substituir valores numéricos long por UUID.fromString("...") - vamos usar um UUID fixo para testes
    # Substituir 1L, 2L, etc. por UUID.fromString("...")
    # Vamos fazer uma substituição simples: trocar todos os números long por UUID.fromString com valores fixos
    # Mas isso é complexo. Vamos fazer uma abordagem mais simples: trocar todos os valores long por UUID.randomUUID()
    # Mas isso pode quebrar testes. Vamos fazer uma substituição manual para os principais casos.
    [System.IO.File]::WriteAllText($file.FullName, $c, (New-Object System.Text.UTF8Encoding $false))
    Write-Output "Atualizado import: $($file.Name)"
}
Write-Output "Concluído!"
