/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.file.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pcc.puppet.enforcer.file.command.FileResourceCreateCommand;
import pcc.puppet.enforcer.file.entity.FileResource;

@Mapper
public interface FileResourceMapper {
  FileResourceMapper INSTANCE = Mappers.getMapper(FileResourceMapper.class);

  FileResource createCommandToFile(FileResourceCreateCommand fileResourceCreateCommand);
}
